from langchain_core.messages import BaseMessage
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder

from Agents.model_factory import build_chat_model
from Schemas.orchestration_schema import TaskGenerationOutput


TASK_GENERATOR_SYSTEM_PROMPT = """
You are a CBAM task generation agent.

Your job:
- Extract the fields needed for CBAM emissions calculation.
- Create English RAG queries for document retrieval.
- Ask the user for missing required information.

Required Java API payload shape:
{{
  "country": "Turkey",
  "cnCode": "25233000",
  "year": 2026,
  "exportVolumeTons": 100
}}

Field rules:
- country default is Turkey if not specified.
- year default is 2026 if not specified.
- exportVolumeTons must come from the user or chat history.
- cnCode must come from the user, product lookup, or previous chat history.
- If cnCode is missing but product_name is available, set product_name and leave cnCode null.
- If both cnCode and product_name are missing, ask the user for product name or CN code.
- If exportVolumeTons is missing, ask the user for export volume in tons.
- Do not invent cnCode.
- Do not invent exportVolumeTons.

CSV rules:
- csv_uploaded may be true if the API says a CSV file was uploaded.
- If csv_uploaded is true, the workflow will use actual emissions calculation.
- If csv_uploaded is false, the workflow will use default emissions calculation.

Retriever query rules:
- english_rag_queries must always be in English.
- Even if the user asks in Turkish, RAG queries must be English.
- Create queries for formulas, reporting guidance, default values, actual emissions, and calculation explanation.
- Do not create Turkish RAG queries.

Language rules:
- language is the final answer language.
- If user writes Turkish, language should be tr.
- If user writes English, language should be en.
- question_to_user must be in the user's language.

Return only structured output.
"""


class CBAMTaskGenerationAgent:
    def __init__(self, model_name: str = "llama3.2", temperature: float = 0.0):
        self.llm = build_chat_model(model_name=model_name, temperature=temperature)

        self.prompt = ChatPromptTemplate.from_messages(
            [
                ("system", TASK_GENERATOR_SYSTEM_PROMPT),
                MessagesPlaceholder("chat_history"),
                ("human", "{user_query}"),
            ]
        )

        self.chain = self.prompt | self.llm.with_structured_output(TaskGenerationOutput)

    def generate(
        self,
        user_query: str,
        chat_history: list[BaseMessage] | None = None,
        csv_uploaded: bool = False,
        csv_file_path: str | None = None,
    ) -> TaskGenerationOutput:
        result = self.chain.invoke(
            {
                "user_query": user_query,
                "chat_history": chat_history or [],
            }
        )

        result.csv_uploaded = csv_uploaded
        result.csv_file_path = csv_file_path

        return self._normalize_result(result)

    @staticmethod
    def _normalize_result(result: TaskGenerationOutput) -> TaskGenerationOutput:
        if not result.language:
            result.language = "en"

        if not result.calculation_input.country:
            result.calculation_input.country = "Turkey"

        if not result.calculation_input.year:
            result.calculation_input.year = 2026

        cleaned_queries = []

        for query in result.english_rag_queries:
            query = str(query).strip()
            if query:
                cleaned_queries.append(query)

        if not cleaned_queries:
            cleaned_queries = [
                "CBAM emissions calculation formula using CN code export volume and embedded emissions",
                "CBAM default values reporting requirements and CN code guidance",
            ]

        result.english_rag_queries = cleaned_queries

        missing = []

        if not result.calculation_input.cnCode and not result.product_name:
            missing.append("cnCode or product_name")

        if result.calculation_input.exportVolumeTons is None:
            missing.append("exportVolumeTons")

        if result.csv_uploaded and not result.csv_file_path:
            missing.append("csv_file_path")

        result.missing_fields = missing

        result.can_calculate = (
            not result.missing_fields
            and result.calculation_input.exportVolumeTons is not None
            and (
                result.calculation_input.cnCode is not None
                or result.product_name is not None
            )
        )

        if result.missing_fields and not result.question_to_user:
            if result.language == "tr":
                result.question_to_user = (
                    "Hesaplama için lütfen eksik bilgileri paylaşın: "
                    + ", ".join(result.missing_fields)
                )
            else:
                result.question_to_user = (
                    "Please provide the missing information: "
                    + ", ".join(result.missing_fields)
                )

        if not result.missing_fields:
            result.question_to_user = None

        return result
