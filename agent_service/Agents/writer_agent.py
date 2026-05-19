from typing import Any

from langchain_core.messages import AIMessage, HumanMessage, SystemMessage
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder

from Agents.llm_output import normalize_llm_content
from Agents.model_factory import build_chat_model
from Schemas.orchestration_schema import QueryType, WorkflowResult


FINAL_WRITER_SYSTEM_PROMPT = """
Write concise user-facing CBAM answers from provided tool results.

Rules:
- Answer in route language: tr -> Turkish, otherwise English.
- Never invent CN codes.
- Never invent default values, emission values, formulas, or legal references.
- Use only task results and RAG excerpts.
- Do not perform arithmetic or claim a calculation was executed.

For normal chat:
- Return the normal answer naturally.

For CBAM answers:
- Use product_cn_lookup and default_value_lookup results for CN codes and default values.
- If product_cn_lookup/default_value_lookup output is a non-empty list, do not say the code was not found.
- For lookup questions, list the best matching CN code first and mention alternatives only if useful.
- Show selected_default_value_tco2e_per_ton when available.
- Use backend_calculation_explanation for formulas and backend steps.
- Use RAG results for policy/methodology context.
- Briefly mention lookup/RAG errors if relevant.
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
