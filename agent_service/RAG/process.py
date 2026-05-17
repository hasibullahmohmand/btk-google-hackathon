from __future__ import annotations

from pathlib import Path
from typing import List, Tuple

from docling.document_converter import DocumentConverter

INPUT_DIR = Path("/data/btk-google-hackathon/pdfs")
OUTPUT_DIR = Path("/data/btk-google-hackathon/pdfs/outputs") / "raw_markdown"

# Ensure output directory exists
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)


# =========================
# DOCLING CONVERSION
# =========================
def convert_to_markdown(file_path: Path) -> str:
    """Converts a PDF or XLSX file to a Markdown string."""
    converter = DocumentConverter()
    result = converter.convert(str(file_path))
    doc = result.document

    markdown = doc.export_to_markdown()
    return markdown


def process_all_files(input_dir: Path, output_dir: Path) -> Tuple[List[Path], List[Path], List[Path]]:
    """
    Processes all PDFs and XLSX files. Tracks saved, empty, and failed files.
    """
    saved_files: List[Path] = []
    empty_files: List[Path] = []
    failed_files: List[Path] = []

    # Gather both PDF and XLSX files
    extensions = ["*.pdf", "*.xlsx"]
    source_files: List[Path] = []
    for ext in extensions:
        source_files.extend(input_dir.glob(ext))
    
    source_files = sorted(source_files, key=lambda p: p.name)

    if not source_files:
        print(f"No PDF or XLSX files found in: {input_dir.resolve()}")
        return saved_files, empty_files, failed_files

    print(f"Found {len(source_files)} files to process.\n" + "-"*50)

    for file_path in source_files:
        file_type = file_path.suffix.upper().replace(".", "")
        print(f"Processing [{file_type}]: {file_path.name}")
        try:
            # 1. Convert file to Markdown text
            markdown_content = convert_to_markdown(file_path)
            
            # 2. Check if the output content is empty or just whitespace
            if not markdown_content.strip():
                print(f"  -> [WARNING] Text detection result is empty!")
                empty_files.append(file_path)
                continue  # Skip saving and move to next file
            
            # 3. Generate the output path and save
            output_path = output_dir / f"{file_path.stem}.md"
            output_path.write_text(markdown_content, encoding="utf-8")
            
            saved_files.append(output_path)
            print(f"  -> Successfully saved: {output_path.name}")
            
        except Exception as exc:
            print(f"  -> Failed: {exc}")
            failed_files.append(file_path)

    return saved_files, empty_files, failed_files


if __name__ == "__main__":
    saved, empty, failed = process_all_files(INPUT_DIR, OUTPUT_DIR)

    # =========================
    # FINAL SUMMARY REPORT
    # =========================
    print("\n" + "="*50)
    print("PROCESSING SUMMARY")
    print("="*50)
    print(f"✅ Successfully Saved: {len(saved)} files")
    print(f"⚠️  Empty/No Text Found: {len(empty)} files")
    print(f"❌ Processing Errors:   {len(failed)} files")
    print("-"*50)

    if empty:
        print("\n🚨 THE FOLLOWING FILES RETURNED EMPTY MARKDOWN (NO TEXT DETECTED):")
        for file in empty:
            print(f"  - {file.name} ({file.suffix.upper()})")
            
    if failed:
        print("\n❌ THE FOLLOWING FILES FAILED TO PROCESS DUE TO ERRORS:")
        for file in failed:
            print(f"  - {file.name}")

    print(f"\nSaved valid Markdown files to: {OUTPUT_DIR.resolve()}")