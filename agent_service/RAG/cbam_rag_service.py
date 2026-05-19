import hashlib
import json
import re
import shutil
from pathlib import Path
from typing import Optional, List

from langchain_chroma import Chroma
from langchain_core.documents import Document
from langchain_community.retrievers import BM25Retriever
from langchain_classic.retrievers import EnsembleRetriever
from langchain_ollama import OllamaEmbeddings


class CBAMRAGService:
    def __init__(
        self,
        markdown_dir: str = "/data/btk-google-hackathon/pdfs/outputs/raw_markdown",
        metadata_path: str = "/data/btk-google-hackathon/pdfs/pdfs-metadata.json",
        chroma_dir: str = "/data/btk-google-hackathon/data/vectorstore/chroma_cbam",
        chunks_path: str = "/data/btk-google-hackathon/data/processed/cbam_chunks.jsonl",
        collection_name: str = "cbam_sections",
        embedding_model: str = "embeddinggemma",
    ):
        self.markdown_dir = Path(markdown_dir)
        self.metadata_path = Path(metadata_path)
        self.chroma_dir = Path(chroma_dir)
        self.chunks_path = Path(chunks_path)
        self.collection_name = collection_name

        self.embedding_model = embedding_model
        self.embed_model = OllamaEmbeddings(model=embedding_model)

        self.pdf_metadata = self.load_pdf_metadata()

    # ---------------------------------------------------------
    # Build database
    # ---------------------------------------------------------

    def build_database(self, reset: bool = True):
        documents = self.process_markdowns()

        if not documents:
            raise ValueError("No documents created.")

        if reset and self.chroma_dir.exists():
            shutil.rmtree(self.chroma_dir)

        self.chroma_dir.mkdir(parents=True, exist_ok=True)
        self.chunks_path.parent.mkdir(parents=True, exist_ok=True)

        self.save_chunks(documents)

        Chroma.from_documents(
            documents=documents,
            embedding=self.embed_model,
            persist_directory=str(self.chroma_dir),
            collection_name=self.collection_name,
        )

        print("CBAM RAG database created.")
        print(f"Chunks: {len(documents)}")
        print(f"Chroma: {self.chroma_dir}")
        print(f"Chunks file: {self.chunks_path}")

    # ---------------------------------------------------------
    # Markdown processing
    # ---------------------------------------------------------

    def process_markdowns(self) -> List[Document]:
        if not self.markdown_dir.exists():
            raise FileNotFoundError(f"Markdown folder not found: {self.markdown_dir}")

        markdown_files = sorted(self.markdown_dir.glob("*.md"))
        documents = []

        for md_file in markdown_files:
            raw_text = md_file.read_text(encoding="utf-8", errors="ignore")
            raw_text = self.clean_text(raw_text)

            if not raw_text:
                continue

            source_file = md_file.name
            source_info = self.get_source_info(source_file)

            source_title = source_info.get("articleName", "")
            source_link = source_info.get("link", "")
            source_pdf_name = source_info.get("pdfName", "")

            source_sector_hint = self.infer_sector_from_title(source_title)
            document_type = self.infer_document_type(source_title, source_file)

            section_docs = self.split_legal_markdown_sections(raw_text)

            for index, section_doc in enumerate(section_docs):
                chunk_text = self.clean_text(section_doc.page_content)

                if self.is_bad_chunk(chunk_text):
                    continue

                heading_path = section_doc.metadata.get("heading_path", "Document Start")

                sector = self.infer_sector(
                    heading=heading_path,
                    text=chunk_text,
                    source_sector_hint=source_sector_hint,
                )

                chunk_id = self.make_id(
                    f"{source_file}|{heading_path}|{index}|{chunk_text[:200]}"
                )

                metadata = {
                    "chunk_id": chunk_id,
                    "source_file": source_file,
                    "source_pdf_name": source_pdf_name,
                    "source_title": source_title,
                    "source_link": source_link,
                    "document_type": document_type,
                    "sector": sector,
                    "source_sector_hint": source_sector_hint or "",
                    "heading_path": heading_path,
                    "h1": section_doc.metadata.get("h1", ""),
                    "h2": section_doc.metadata.get("h2", ""),
                    "h3": section_doc.metadata.get("h3", ""),
                    "h4": section_doc.metadata.get("h4", ""),
                    "page": section_doc.metadata.get("page", ""),
                    "cn_codes": self.extract_cn_codes(chunk_text),
                }

                documents.append(
                    Document(
                        page_content=chunk_text,
                        metadata=self.safe_metadata(metadata),
                    )
                )

        return documents

    def split_legal_markdown_sections(self, text: str) -> List[Document]:
        """
        Docling-friendly parser.

        Important:
        - Headings are attached to the next real body text.
        - Heading-only chunks are skipped.
        - Page markers are captured if they exist in Markdown.
        """

        lines = text.splitlines()

        documents = []
        heading_stack = []
        current_body = []
        current_page = ""

        def flush():
            nonlocal current_body

            body = self.clean_text("\n".join(current_body))

            if not body:
                current_body = []
                return

            if self.is_bad_chunk(body):
                current_body = []
                return

            heading_path = " > ".join(heading_stack) if heading_stack else "Document Start"
            full_text = self.clean_text(f"{heading_path}\n\n{body}")

            documents.append(
                Document(
                    page_content=full_text,
                    metadata={
                        "heading_path": heading_path,
                        "h1": heading_stack[0] if len(heading_stack) > 0 else "",
                        "h2": heading_stack[1] if len(heading_stack) > 1 else "",
                        "h3": heading_stack[2] if len(heading_stack) > 2 else "",
                        "h4": heading_stack[3] if len(heading_stack) > 3 else "",
                        "page": current_page,
                    },
                )
            )

            current_body = []

        for line in lines:
            stripped = line.strip()

            detected_page = self.detect_page_marker(stripped)
            if detected_page:
                current_page = detected_page
                continue

            heading_match = re.match(r"^(#{1,6})\s+(.+?)\s*$", stripped)

            if heading_match:
                flush()

                level = len(heading_match.group(1))
                title = heading_match.group(2).strip()

                heading_stack = heading_stack[: level - 1]
                heading_stack.append(title)

                continue

            current_body.append(line)

        flush()

        return documents

    # ---------------------------------------------------------
    # Retriever
    # ---------------------------------------------------------

    def build_retriever(
        self,
        sector: Optional[str] = None,
        k: int = 5,
        fetch_k: int = 20,
    ):
        chunks = self.load_chunks()

        if sector:
            bm25_docs = [
                doc for doc in chunks
                if doc.metadata.get("sector") in [sector, "cross_sectoral"]
            ]

            chroma_filter = {
                "$or": [
                    {"sector": sector},
                    {"sector": "cross_sectoral"},
                ]
            }
        else:
            bm25_docs = chunks
            chroma_filter = None

        if not bm25_docs:
            raise ValueError(f"No BM25 documents found for sector={sector}")

        vectorstore = Chroma(
            persist_directory=str(self.chroma_dir),
            collection_name=self.collection_name,
            embedding_function=self.embed_model,
        )

        chroma_retriever = vectorstore.as_retriever(
            search_type="mmr",
            search_kwargs={
                "k": k,
                "fetch_k": fetch_k,
                "filter": chroma_filter,
            },
        )

        bm25_retriever = BM25Retriever.from_documents(bm25_docs)
        bm25_retriever.k = k

        return EnsembleRetriever(
            retrievers=[chroma_retriever, bm25_retriever],
            weights=[0.55, 0.45],
        )

    def retrieve(
        self,
        query: str,
        sector: Optional[str] = None,
        k: int = 5,
        fetch_k: int = 20,
    ) -> List[Document]:
        retriever = self.build_retriever(
            sector=sector,
            k=k,
            fetch_k=fetch_k,
        )

        docs = retriever.invoke(query)
        docs = self.deduplicate(docs)

        return docs[:k]

    def retrieve_general(self, query: str, k: int = 5) -> List[Document]:
        """
        Best for:
        - What is CBAM?
        - Who needs to report?
        - What is the transitional period?
        - What are default values?
        """

        expanded_query = self.expand_general_query(query)

        retriever = self.build_retriever(
            sector=None,
            k=20,
            fetch_k=40,
        )

        docs = retriever.invoke(expanded_query)
        docs = self.deduplicate(docs)

        scored_docs = []

        for doc in docs:
            if self.is_low_value_result(doc):
                continue

            score = self.score_general_result(doc, query)
            scored_docs.append((score, doc))

        scored_docs.sort(key=lambda item: item[0], reverse=True)

        return [doc for _, doc in scored_docs[:k]]

    # ---------------------------------------------------------
    # General retrieval scoring
    # ---------------------------------------------------------

    def expand_general_query(self, query: str) -> str:
        q = query.lower().strip()

        if q in ["what is cbam", "what is cbam?"]:
            return (
                "What is the EU Carbon Border Adjustment Mechanism CBAM? "
                "definition purpose carbon price greenhouse gas emissions "
                "carbon-intensive goods imported into the EU"
            )

        return query

    def score_general_result(self, doc: Document, query: str) -> int:
        text = doc.page_content.lower()
        meta = doc.metadata

        title = (meta.get("source_title") or "").lower()
        heading = (meta.get("heading_path") or "").lower()
        document_type = meta.get("document_type")
        sector = meta.get("sector")

        score = 0

        if "what is the eu cbam" in heading:
            score += 10

        if "what is cbam" in heading:
            score += 10

        if "carbon border adjustment mechanism" in text:
            score += 8

        if "put a fair price" in text:
            score += 6

        if "greenhouse gas" in text or "ghg" in text:
            score += 4

        if "imported into the eu" in text or "entering the eu" in text:
            score += 4

        if document_type in ["factsheet", "guidance", "faq"]:
            score += 3

        if document_type == "regulation":
            score += 2

        if sector == "cross_sectoral":
            score += 3

        # For general CBAM definition, sector-specific factsheets are useful
        # but should not dominate all results.
        if sector in ["cement", "aluminium", "hydrogen", "electricity", "fertiliser", "iron_and_steel"]:
            score -= 1

        bad_title_terms = [
            "registration guide",
            "user manual",
            "access request",
            "declarant portal",
            "operators portal",
        ]

        if any(term in title for term in bad_title_terms):
            score -= 20

        bad_heading_terms = [
            "step 1",
            "step 2",
            "access request",
            "create eu login",
            "transitional registry",
            "structure",
        ]

        if any(term in heading for term in bad_heading_terms):
            score -= 10

        return score

    def is_low_value_result(self, doc: Document) -> bool:
        text = doc.page_content.lower()
        meta = doc.metadata

        title = (meta.get("source_title") or "").lower()
        heading = (meta.get("heading_path") or "").lower()

        bad_phrases = [
            "overview of recent changes",
            "answers updated",
            "questions added",
            "document last updated",
            "last updated on",
        ]

        if any(phrase in text for phrase in bad_phrases):
            return True

        bad_title_terms = [
            "registration guide",
            "user manual",
            "access request",
            "declarant portal",
            "operators portal",
        ]

        if any(term in title for term in bad_title_terms):
            return True

        bad_heading_terms = [
            "step 1",
            "step 2",
            "create eu login account",
            "registration guide",
            "access request",
            "user manual",
            "declarant portal",
            "structure",
        ]

        if any(term in heading for term in bad_heading_terms):
            return True

        plain = re.sub(r"\s+", " ", text).strip()

        if len(plain) < 120:
            return True

        return False

    # ---------------------------------------------------------
    # Sector logic
    # ---------------------------------------------------------

    def infer_sector(
        self,
        heading: str,
        text: str,
        source_sector_hint: Optional[str],
    ) -> str:
        heading_sector = self.infer_sector_from_heading(heading)

        if heading_sector:
            return heading_sector

        cn_sector = self.infer_sector_from_cn_codes(text)

        if cn_sector:
            return cn_sector

        if source_sector_hint:
            return source_sector_hint

        return "cross_sectoral"

    def infer_sector_from_title(self, title: str) -> Optional[str]:
        title = title.lower()

        if "cement" in title:
            return "cement"

        if "aluminium" in title or "aluminum" in title:
            return "aluminium"

        if "iron" in title or "steel" in title:
            return "iron_and_steel"

        if "fertiliser" in title or "fertilizer" in title:
            return "fertiliser"

        if "hydrogen" in title:
            return "hydrogen"

        if "electricity" in title:
            return "electricity"

        return None

    def infer_sector_from_heading(self, heading: str) -> Optional[str]:
        heading = heading.lower()

        heading_map = {
            "cement": [
                "cement",
                "cement clinker",
                "clinker",
                "calcined clay",
                "aluminous cement",
            ],
            "iron_and_steel": [
                "iron and steel",
                "sintered ore",
                "pig iron",
                "crude steel",
                "iron or steel products",
                "direct reduced iron",
                "dri",
                "ferro-manganese",
                "ferro-chromium",
                "ferro-nickel",
                "femn",
                "fecr",
                "feni",
                "electric arc furnace",
                "basic oxygen steelmaking",
            ],
            "aluminium": [
                "aluminium",
                "aluminum",
                "unwrought aluminium",
                "aluminium products",
                "primary aluminium",
                "secondary aluminium",
            ],
            "fertiliser": [
                "fertiliser",
                "fertilizer",
                "ammonia",
                "nitric acid",
                "urea",
                "mixed fertilisers",
                "mixed fertilizers",
            ],
            "hydrogen": [
                "hydrogen",
            ],
            "electricity": [
                "electricity",
                "electrical energy",
            ],
        }

        for sector, words in heading_map.items():
            if any(word in heading for word in words):
                return sector

        return None

    def infer_sector_from_cn_codes(self, text: str) -> Optional[str]:
        text = text.lower()

        cn_map = {
            "cement": ["2507", "2523"],
            "electricity": ["2716"],
            "fertiliser": ["2808", "2814", "2834", "3102", "3105"],
            "hydrogen": ["2804 10 00", "28041000"],
            "iron_and_steel": ["2601", "720", "721", "722", "730", "731"],
            "aluminium": ["760", "761"],
        }

        for sector, codes in cn_map.items():
            if any(code in text for code in codes):
                return sector

        return None

    # ---------------------------------------------------------
    # PDF metadata
    # ---------------------------------------------------------

    def load_pdf_metadata(self) -> dict:
        if not self.metadata_path.exists():
            return {}

        with self.metadata_path.open("r", encoding="utf-8") as f:
            rows = json.load(f)

        metadata = {}

        for row in rows:
            pdf_name = row.get("pdfName", "")

            if not pdf_name:
                continue

            for key in self.filename_keys(pdf_name):
                metadata[key] = row

        return metadata

    def get_source_info(self, source_file: str) -> dict:
        for key in self.filename_keys(source_file):
            if key in self.pdf_metadata:
                return self.pdf_metadata[key]

        return {
            "pdfName": "",
            "articleName": "",
            "link": "",
        }

    def filename_keys(self, filename: str) -> List[str]:
        name = Path(filename).name

        variants = set()
        variants.add(name)

        if name.endswith(".md"):
            variants.add(name[:-3])
            variants.add(name[:-3] + ".pdf")

        if name.endswith("_TXT.md"):
            variants.add(name.replace("_TXT.md", ".pdf"))
            variants.add(name.replace("_TXT.md", ""))

        return [self.normalize_filename(v) for v in variants]

    def normalize_filename(self, filename: str) -> str:
        filename = filename.lower()
        filename = filename.replace("%20", " ")
        filename = filename.replace("+", " ")
        filename = filename.replace("&", "and")
        filename = re.sub(r"\.(pdf|md|txt)$", "", filename)
        filename = re.sub(r"[_\-]+", " ", filename)
        filename = re.sub(r"\s+", " ", filename)
        return filename.strip()

    # ---------------------------------------------------------
    # Save/load chunks for BM25
    # ---------------------------------------------------------

    def save_chunks(self, documents: List[Document]):
        with self.chunks_path.open("w", encoding="utf-8") as f:
            for doc in documents:
                row = {
                    "page_content": doc.page_content,
                    "metadata": doc.metadata,
                }

                f.write(json.dumps(row, ensure_ascii=False) + "\n")

    def load_chunks(self) -> List[Document]:
        if not self.chunks_path.exists():
            raise FileNotFoundError("Run build_database() first.")

        documents = []

        with self.chunks_path.open("r", encoding="utf-8") as f:
            for line in f:
                row = json.loads(line)

                documents.append(
                    Document(
                        page_content=row["page_content"],
                        metadata=row["metadata"],
                    )
                )

        return documents

    # ---------------------------------------------------------
    # Helpers
    # ---------------------------------------------------------

    def detect_page_marker(self, line: str) -> str:
        """
        Tries to detect page numbers from common Markdown/PDF conversion patterns.

        Supported examples:
        <!-- Page 3 -->
        <!-- page: 3 -->
        Page 3
        - Page 3 -
        [Page 3]
        """

        patterns = [
            r"<!--\s*page\s*:?\s*(\d+)\s*-->",
            r"<!--\s*Page\s*:?\s*(\d+)\s*-->",
            r"^\s*page\s+(\d+)\s*$",
            r"^\s*Page\s+(\d+)\s*$",
            r"^\s*-\s*Page\s+(\d+)\s*-\s*$",
            r"^\s*\[Page\s+(\d+)\]\s*$",
        ]

        for pattern in patterns:
            match = re.search(pattern, line)

            if match:
                return match.group(1)

        return ""

    def is_bad_chunk(self, text: str) -> bool:
        cleaned = text.strip()

        if not cleaned:
            return True

        plain_text = re.sub(r"#+", "", cleaned)
        plain_text = re.sub(r"\s+", " ", plain_text).strip()

        if len(plain_text) < 80:
            return True

        return False

    def infer_document_type(self, title: str, source_file: str) -> str:
        blob = f"{title} {source_file}".lower()

        if "regulation" in blob or "oj_l" in blob or "celex" in blob:
            return "regulation"

        if "faq" in blob or "questions and answers" in blob:
            return "faq"

        if "factsheet" in blob:
            return "factsheet"

        if "guidance" in blob or "guide" in blob:
            return "guidance"

        if "default value" in blob or "default values" in blob:
            return "default_values_note"

        return "unknown"

    def extract_cn_codes(self, text: str) -> str:
        codes = sorted(set(re.findall(r"\b\d{4}(?:\s?\d{2})?(?:\s?\d{2})?\b", text)))
        return ", ".join(codes[:50])

    def clean_text(self, text: str) -> str:
        text = text.replace("\r\n", "\n").replace("\r", "\n")
        text = re.sub(r"\n{3,}", "\n\n", text)

        return text.strip()

    def make_id(self, text: str) -> str:
        return hashlib.sha256(text.encode("utf-8")).hexdigest()

    def safe_metadata(self, metadata: dict) -> dict:
        clean = {}

        for key, value in metadata.items():
            if value is None:
                clean[key] = ""
            elif isinstance(value, (str, int, float, bool)):
                clean[key] = value
            else:
                clean[key] = str(value)

        return clean

    def deduplicate(self, docs: List[Document]) -> List[Document]:
        seen = set()
        unique = []

        for doc in docs:
            chunk_id = doc.metadata.get("chunk_id") or self.make_id(doc.page_content[:300])

            if chunk_id in seen:
                continue

            seen.add(chunk_id)
            unique.append(doc)

        return unique

    def print_docs(self, docs: List[Document], max_chars: int = 1200):
        for i, doc in enumerate(docs, start=1):
            meta = doc.metadata

            print("=" * 100)
            print(f"RESULT {i}")
            print(f"Title: {meta.get('source_title')}")
            print(f"Link: {meta.get('source_link')}")
            print(f"PDF: {meta.get('source_pdf_name')}")
            print(f"Document type: {meta.get('document_type')}")
            print(f"Sector: {meta.get('sector')}")
            print(f"Heading: {meta.get('heading_path')}")
            print(f"CN codes: {meta.get('cn_codes')}")
            print(f"Page: {meta.get('page')}")
            print("-" * 100)
            print(doc.page_content[:max_chars])
            print()


if __name__ == "__main__":
    service = CBAMRAGService()
    service.build_database(reset=True)