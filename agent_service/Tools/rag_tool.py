from RAG.cbam_rag_service import CBAMRAGService


class CBAMRAGTool:
    def __init__(self):
        self.rag = CBAMRAGService()

    def search_general(self, query: str, k: int = 5):
        return self.rag.retrieve_general(query=query, k=k)

    def search_sector(self, query: str, sector: str, k: int = 5):
        return self.rag.retrieve(query=query, sector=sector, k=k)