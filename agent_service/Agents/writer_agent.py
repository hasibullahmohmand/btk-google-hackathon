from typing import Any

from langchain_core.messages import AIMessage, HumanMessage, SystemMessage
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder

from Agents.llm_output import normalize_llm_content
from Agents.model_factory import build_chat_model
from Schemas.orchestration_schema import QueryType, WorkflowResult


FINAL_WRITER_SYSTEM_PROMPT = """
You are a multilingual CBAM final answer writer.

You receive:
- chat history
- original user query
- route result
- task generation result
- tool results

Rules:
- Answer in the user's language.
- If language is tr, answer in Turkish.
- If language is en, answer in English.
- Keep the answer practical and concise.
- Never invent CN codes.
- Never invent emission values.
- Never invent legal references.
- Use only the given task results.

For normal chat:
- Return the normal answer naturally.

For CBAM answers:
- Explain whether the calculation used default values or uploaded CSV actual emissions.
- If CSV was uploaded, say that the calculation used uploaded CSV data.
- If CSV was not uploaded, say that the calculation used default emissions calculation.
- Show the Java API payload when available.
- Show product lookup result if available.
- Show calculation result if available.
- If calculation failed, explain the exact tool error.
- If information is missing, ask only for the missing fields.
- If RAG results are available, briefly explain the formula or guidance from them.
- Do not output huge raw RAG chunks.
"""


class CBAMWriterAgent:
    def __init__(self, model_name: str = "llama3.2", temperature: float = 0.0):
        self.llm = build_chat_model(model_name=model_name, temperature=temperature)

        self.prompt = ChatPromptTemplate.from_messages(
            [
                ("system", FINAL_WRITER_SYSTEM_PROMPT),
                MessagesPlaceholder("chat_history"),
                ("human", "{writer_input}"),
            ]
        )

        self.chain = self.prompt | self.llm

    def write(self, workflow_result: WorkflowResult) -> str:
        if workflow_result.route.query_type == QueryType.NORMAL:
            return workflow_result.normal_answer or "Hello! I am a CBAM assistant."

        writer_input = {
            "user_query": workflow_result.user_query,
            "route": self._model_to_dict(workflow_result.route),
            "task_generation": self._model_to_dict(workflow_result.task_generation),
            "task_results": [
                self._model_to_dict(item)
                for item in workflow_result.task_results
            ],
        }

        chat_history = self._dicts_to_messages(workflow_result.chat_history)
        chat_history = chat_history[-10:]

        response = self.chain.invoke(
            {
                "chat_history": chat_history,
                "writer_input": str(writer_input),
            }
        )

        return normalize_llm_content(response.content)

    @staticmethod
    def _dicts_to_messages(history: list[dict[str, str]]):
        messages = []

        for item in history:
            role = item.get("role")
            content = item.get("content", "")

            if not content:
                continue

            if role == "user":
                messages.append(HumanMessage(content=content))
            elif role == "system":
                messages.append(SystemMessage(content=content))
            else:
                messages.append(AIMessage(content=content))

        return messages

    @staticmethod
    def _model_to_dict(model: Any) -> dict | None:
        if model is None:
            return None

        if hasattr(model, "model_dump"):
            return model.model_dump()

        if hasattr(model, "dict"):
            return model.dict()

        return dict(model)
