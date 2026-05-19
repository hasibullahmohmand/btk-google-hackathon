from langchain_core.messages import BaseMessage
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_ollama import ChatOllama

from Schemas.orchestration_schema import QueryType, RouteOutput


ROUTER_SYSTEM_PROMPT = """
You are a multilingual router for a specialized CBAM assistant.

Classify the latest user message as:

normal:
- greetings
- casual chat
- who are you / what can you do
- messages that do not need CBAM document retrieval or CN-code lookup

cbam:
- CBAM regulations
- CBAM calculation methods or formulas
- embedded emissions
- direct emissions
- indirect emissions
- default values
- CN codes
- CBAM sectors
- reporting
- transitional or definitive period
- importer/exporter obligations
- anything requiring RAG, CN-code lookup, or backend formula explanation

Use chat history only to understand follow-up messages.
Return only structured output.
"""


class CBAMOrchestrator:
    def __init__(self, model_name: str = "llama3.2", temperature: float = 0.0):
        self.model_name = model_name
        self.temperature = temperature
        self.llm = ChatOllama(model=model_name, temperature=temperature)

        self.prompt = ChatPromptTemplate.from_messages(
            [
                ("system", ROUTER_SYSTEM_PROMPT),
                MessagesPlaceholder("chat_history"),
                ("human", "{user_query}"),
            ]
        )

        self.chain = self.prompt | self.llm.with_structured_output(RouteOutput)

    def route(
        self,
        user_query: str,
        chat_history: list[BaseMessage] | None = None,
    ) -> RouteOutput:
        try:
            result = self.chain.invoke(
                {
                    "user_query": user_query,
                    "chat_history": chat_history or [],
                }
            )

            if result.query_type not in {QueryType.NORMAL, QueryType.CBAM}:
                result.query_type = QueryType.NORMAL

            result.language = result.language or _detect_language(user_query)
            result = _apply_rule_overrides(
                route=result,
                user_query=user_query,
                chat_history=chat_history,
            )

            return result

        except Exception:
            return self._fallback_route(
                user_query=user_query,
                chat_history=chat_history,
            )

    @staticmethod
    def _fallback_route(
        user_query: str,
        chat_history: list[BaseMessage] | None = None,
    ) -> RouteOutput:
        text = user_query.casefold().strip()
        language = _detect_language(text)

        if text in _NORMAL_MESSAGES:
            return RouteOutput(query_type=QueryType.NORMAL, language=language)

        if any(term in text for term in _CBAM_TERMS):
            return RouteOutput(query_type=QueryType.CBAM, language=language)

        if chat_history:
            return RouteOutput(query_type=QueryType.CBAM, language=language)

        return RouteOutput(query_type=QueryType.NORMAL, language=language)


_NORMAL_MESSAGES = {
    "",
    "hi",
    "hello",
    "hey",
    "merhaba",
    "selam",
    "slm",
    "what can you do",
    "what can you do?",
    "who are you",
    "who are you?",
    "sen kimsin",
    "sen kimsin?",
    "ne yapabilirsin",
    "ne yapabilirsin?",
}

_CBAM_TERMS = {
    "cbam",
    "carbon border",
    "carbon border adjustment",
    "emission",
    "emissions",
    "embedded",
    "direct emission",
    "indirect emission",
    "default value",
    "cn code",
    "cn_code",
    "cncode",
    "calculate",
    "calculation",
    "report",
    "reporting",
    "transitional",
    "definitive",
    "cement",
    "aluminium",
    "aluminum",
    "fertiliser",
    "fertilizer",
    "iron",
    "steel",
    "hydrogen",
    "electricity",
    "karbon",
    "emisyon",
    "gömülü",
    "dogrudan",
    "doğrudan",
    "dolaylı",
    "varsayılan",
    "hesap",
    "rapor",
    "geçiş",
    "çimento",
    "alüminyum",
    "gübre",
    "demir",
    "çelik",
    "hidrojen",
    "elektrik",
}


def _looks_turkish(text: str) -> bool:
    turkish_chars = {"ı", "ğ", "ü", "ş", "ö", "ç"}
    turkish_words = {"merhaba", "selam", "nedir", "nasıl", "hesap", "rapor"}

    return any(char in text for char in turkish_chars) or any(
        word in text.split() for word in turkish_words
    )


def _detect_language(text: str) -> str:
    return "tr" if _looks_turkish(text.casefold()) else "en"


def _apply_rule_overrides(
    route: RouteOutput,
    user_query: str,
    chat_history: list[BaseMessage] | None = None,
) -> RouteOutput:
    text = user_query.casefold().strip()

    if text in _NORMAL_MESSAGES:
        route.query_type = QueryType.NORMAL
        return route

    if any(term in text for term in _CBAM_TERMS):
        route.query_type = QueryType.CBAM
        return route

    if route.query_type == QueryType.NORMAL and chat_history:
        route.query_type = QueryType.CBAM

    return route
