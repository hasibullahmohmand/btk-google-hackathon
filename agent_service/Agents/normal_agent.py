from langchain_core.messages import BaseMessage

from Schemas.orchestration_schema import NormalAnswer


class NormalAgent:
    def __init__(self, model_name: str = "llama3.2", temperature: float = 0.0):
        self.model_name = model_name
        self.temperature = temperature

    def answer(
        self,
        user_query: str,
        chat_history: list[BaseMessage] | None = None,
    ) -> NormalAnswer:
        language = "tr" if _looks_turkish(user_query) else "en"

        if language == "tr":
            answer = (
                "Merhaba, CBAM konusunda yardımcı olabilirim. "
                "Şunları sorabilirsiniz: CBAM nedir? Bir ürünün CN kodu nedir? "
                "Çimento veya alüminyum gibi ürün adından CN kodu bulabilir misin? "
                "25233000 CN kodu hangi ürüne ait? "
                "CBAM hesaplaması backend'de hangi adımlarla yapılır? "
                "Varsayılan değerler ve raporlama kuralları nasıl kullanılır?"
            )
        else:
            answer = (
                "Hi, I can help with CBAM. You can ask things like: "
                "What is CBAM? What is the CN code for this product? "
                "Can you find the CN code for cement or aluminium? "
                "What product does CN code 25233000 refer to? "
                "How is a CBAM calculation done in the backend? "
                "How do default values and reporting rules work?"
            )

        return NormalAnswer(language=language, answer=answer)


def _looks_turkish(text: str) -> bool:
    normalized = text.casefold()
    return any(char in normalized for char in {"ı", "ğ", "ü", "ş", "ö", "ç"}) or any(
        word in normalized.split()
        for word in {"merhaba", "selam", "nedir", "nasıl", "hesap"}
    )
