from langchain_core.messages import BaseMessage
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_ollama import ChatOllama

from Schemas.orchestration_schema import NormalAnswer


NORMAL_AGENT_SYSTEM_PROMPT = """
You are a friendly multilingual CBAM chatbot.

You handle only normal casual chat.

Rules:
- Answer in the user's language.
- Keep the answer short.
- If the user greets you, greet them back.
- Briefly explain that you can help with CBAM calculations, default values, CN codes, regulations, CSV-based actual emissions, and reporting.
- Do not answer detailed CBAM questions here.
"""


class NormalAgent:
    def __init__(self, model_name: str = "llama3.2", temperature: float = 0.0):
        self.llm = ChatOllama(model=model_name, temperature=temperature)

        self.prompt = ChatPromptTemplate.from_messages(
            [
                ("system", NORMAL_AGENT_SYSTEM_PROMPT),
                MessagesPlaceholder("chat_history"),
                ("human", "{user_query}"),
            ]
        )

        self.chain = self.prompt | self.llm.with_structured_output(NormalAnswer)

    def answer(
        self,
        user_query: str,
        chat_history: list[BaseMessage] | None = None,
    ) -> NormalAnswer:
        return self.chain.invoke(
            {
                "user_query": user_query,
                "chat_history": chat_history or [],
            }
        )