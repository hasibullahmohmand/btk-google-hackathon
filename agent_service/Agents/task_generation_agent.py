from langchain_core.messages import BaseMessage
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder

from Agents.model_factory import build_chat_model
from Schemas.orchestration_schema import TaskGenerationOutput


TASK_GENERATOR_SYSTEM_PROMPT = """
Extract structured context for a CBAM explanation-only agent.

Return:
- language: en or tr.
- product_name: product text for CN-code/default-value lookup, if present.
- calculation_input.cnCode: only if user gives a CN code.
- country: default Turkey.
- year: default 2026.
- exportVolumeTons: only if user gives a volume.
- english_rag_queries: concise English queries for CBAM definitions, formulas,
  default values, actual emissions, CN-code guidance, and reporting.

Rules:
- Never invent CN codes, volumes, or emission values.
- can_calculate must be false.
- Ask for product name/CN code only when the user explicitly asks for CN lookup
  but gives neither.
- Include formula queries for calculation questions.

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
    ) -> TaskGenerationOutput:
        result = self.chain.invoke(
            {
                "user_query": user_query,
                "chat_history": chat_history or [],
            }
        )

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

        formula_queries = [
            "CBAM embedded emissions formula direct indirect emissions specific emissions export volume",
            "CBAM actual emissions activity data amount emission factor production volume formula",
            "CBAM default values embedded emissions export volume default value formula",
        ]

        for query in formula_queries:
            if query not in cleaned_queries:
                cleaned_queries.append(query)

        result.english_rag_queries = cleaned_queries

        missing = []

        question_text = " ".join(
            [
                str(result.product_name or ""),
                *result.english_rag_queries,
            ]
        ).casefold()
        wants_cn_lookup = any(
            term in question_text
            for term in ["cn code", "cn_code", "cncode", "commodity code"]
        )

        if (
            wants_cn_lookup
            and not result.calculation_input.cnCode
            and not result.product_name
        ):
            missing.append("cnCode or product_name")

        result.missing_fields = missing
        result.can_calculate = False

        if result.missing_fields and not result.question_to_user:
            if result.language == "tr":
                result.question_to_user = (
                    "CN kodunu bulabilmem için lütfen şu bilgiyi paylaşın: "
                    + ", ".join(result.missing_fields)
                )
            else:
                result.question_to_user = (
                    "Please provide this information so I can look up the CN code: "
                    + ", ".join(result.missing_fields)
                )

        if not result.missing_fields:
            result.question_to_user = None

        return result
