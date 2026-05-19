from langchain_core.messages import BaseMessage
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_ollama import ChatOllama

from Schemas.orchestration_schema import QueryType, RouteOutput


ROUTER_SYSTEM_PROMPT = """
You are a multilingual router for a CBAM chatbot.

Classify the latest user message as:

normal:
- greetings
- casual chat
- questions like who are you
- messages that do not need CBAM tools

cbam:
- CBAM regulations
- CBAM calculations
- embedded emissions
- direct emissions
- indirect emissions
- default values
- CN codes
- CBAM sectors
- reporting
- transitional or definitive period
- carbon price adjustment
- importer/exporter obligations
- anything requiring document retrieval, default value lookup, CSV calculation, or emissions calculation

Use chat history only to understand follow-up messages.

Examples:
- hello -> normal
- merhaba -> normal
- what can you do? -> normal
- calculate CBAM emissions for 100 tons -> cbam
- what is the default value for cement? -> cbam
- now calculate it for 10 tons -> cbam if previous context is CBAM

Return only structured output.
"""


class CBAMOrchestrator:
    def __init__(self, model_name: str = "llama3.2", temperature: float = 0.0):
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
        result = self.chain.invoke(
            {
                "user_query": user_query,
                "chat_history": chat_history or [],
            }
        )

        if result.query_type not in {QueryType.NORMAL, QueryType.CBAM}:
            result.query_type = QueryType.NORMAL

        return result