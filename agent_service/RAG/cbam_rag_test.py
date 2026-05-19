from cbam_rag_service import CBAMRAGService


if __name__ == "__main__":
    service = CBAMRAGService()

    docs = service.retrieve(
        query="how to calculte cement emission",
        sector="cement",
        k=5,
    )

    service.print_docs(docs)