from typing import Annotated, Any, TypedDict

from langchain_core.messages import AIMessage, AnyMessage, HumanMessage
from langgraph.checkpoint.memory import InMemorySaver
from langgraph.graph import END, StateGraph
from langgraph.graph.message import add_messages

from Agents.normal_agent import NormalAgent
from Agents.orchestrator import CBAMOrchestrator
from Agents.task_generation_agent import CBAMTaskGenerationAgent
from Schemas.orchestration_schema import (
    QueryType,
    RouteOutput,
    TaskGenerationOutput,
    TaskResult,
    WorkflowResult,
)
from Tools.excel_tool import CBAMDefaultValuesTool
from Tools.rag_tool import CBAMRAGTool


class CBAMWorkflowState(TypedDict):
    messages: Annotated[list[AnyMessage], add_messages]
    user_query: str
    route: dict[str, Any] | None
    normal_answer: str | None
    task_generation: dict[str, Any] | None
    task_results: list[dict[str, Any]]
    final_answer: str | None


class CBAMWorkflow:
    def __init__(
        self,
        model_name: str = "llama3.2",
        cbam_model_name: str = "gemini:gemini-2.5-flash",
        temperature: float = 0.0,
    ):
        self.router = CBAMOrchestrator(model_name=model_name, temperature=temperature)
        self.normal_agent = NormalAgent(model_name=model_name, temperature=temperature)
        self.task_agent = CBAMTaskGenerationAgent(
            model_name=cbam_model_name,
            temperature=temperature,
        )

        self.rag_tool = CBAMRAGTool()
        self.default_values_tool = self._build_default_values_tool()

        self.memory = InMemorySaver()
        self.graph = self._build_graph()

    def _build_graph(self):
        graph = StateGraph(CBAMWorkflowState)

        graph.add_node("route", self._route_node)
        graph.add_node("normal", self._normal_node)
        graph.add_node("generate_tasks", self._generate_tasks_node)
        graph.add_node("execute_tasks", self._execute_tasks_node)

        graph.set_entry_point("route")

        graph.add_conditional_edges(
            "route",
            self._route_condition,
            {
                "normal": "normal",
                "cbam": "generate_tasks",
            },
        )

        graph.add_edge("normal", END)
        graph.add_edge("generate_tasks", "execute_tasks")
        graph.add_edge("execute_tasks", END)

        return graph.compile(checkpointer=self.memory)

    def _route_node(self, state: CBAMWorkflowState) -> dict:
        all_messages = state.get("messages", [])
        chat_history = all_messages[:-1] if all_messages else []
        chat_history = chat_history[-8:]

        route = self.router.route(
            user_query=state["user_query"],
            chat_history=chat_history,
        )

        return {
            "route": self._model_to_dict(route),
        }

    def _route_condition(self, state: CBAMWorkflowState) -> str:
        route = self._parse_route(state["route"])

        if route.query_type == QueryType.NORMAL:
            return "normal"

        return "cbam"

    def _normal_node(self, state: CBAMWorkflowState) -> dict:
        all_messages = state.get("messages", [])
        chat_history = all_messages[:-1] if all_messages else []
        chat_history = chat_history[-8:]

        normal_answer = self.normal_agent.answer(
            user_query=state["user_query"],
            chat_history=chat_history,
        )

        return {
            "normal_answer": normal_answer.answer,
            "final_answer": normal_answer.answer,
            "messages": [AIMessage(content=normal_answer.answer)],
        }

    def _generate_tasks_node(self, state: CBAMWorkflowState) -> dict:
        all_messages = state.get("messages", [])
        chat_history = all_messages[:-1] if all_messages else []
        chat_history = chat_history[-8:]

        task_generation = self.task_agent.generate(
            user_query=state["user_query"],
            chat_history=chat_history,
        )

        task_generation = self._patch_task_generation_from_text(
            task_generation=task_generation,
            messages=all_messages,
        )

        if not task_generation.product_name and self.default_values_tool is not None:
            inferred_product_name = self._infer_product_name_from_lookup(
                state["user_query"]
            )

            if inferred_product_name:
                task_generation.product_name = inferred_product_name

        return {
            "task_generation": self._model_to_dict(task_generation),
        }

    def _execute_tasks_node(self, state: CBAMWorkflowState) -> dict:
        task_generation = self._parse_task_generation(state["task_generation"])

        if task_generation is None:
            return {"task_results": []}

        task_results: list[TaskResult] = []

        if task_generation.question_to_user and task_generation.missing_fields:
            task_results.append(
                TaskResult(
                    name="missing_information",
                    output={
                        "missing_fields": task_generation.missing_fields,
                        "question_to_user": task_generation.question_to_user,
                    },
                    success=False,
                    error="Missing required information.",
                )
            )

            return {
                "task_results": [self._model_to_dict(item) for item in task_results],
                "final_answer": task_generation.question_to_user,
                "messages": [AIMessage(content=task_generation.question_to_user)],
            }

        if not task_generation.calculation_input.cnCode and task_generation.product_name:
            if self.default_values_tool is None:
                task_results.append(
                    TaskResult(
                        name="product_cn_lookup",
                        output=None,
                        success=False,
                        error="CN lookup data is not available.",
                    )
                )
            else:
                try:
                    lookup_result = self.default_values_tool.lookup_by_product_name(
                        product_name=task_generation.product_name,
                        year=task_generation.calculation_input.year,
                        country=task_generation.calculation_input.country,
                        limit=5,
                    )

                    task_results.append(
                        TaskResult(
                            name="product_cn_lookup",
                            output=self._compact_cn_lookup(lookup_result),
                            success=True,
                        )
                    )

                    if lookup_result:
                        first = lookup_result[0]
                        cn_code = first.get("cn_code") or first.get("cnCode")

                        if cn_code:
                            task_generation.calculation_input.cnCode = str(cn_code)

                except Exception as exc:
                    task_results.append(
                        TaskResult(
                            name="product_cn_lookup",
                            output=None,
                            success=False,
                            error=str(exc),
                        )
                    )

        if task_generation.calculation_input.cnCode:
            if self.default_values_tool is None:
                task_results.append(
                    TaskResult(
                        name="cn_code_lookup",
                        output=None,
                        success=False,
                        error="CN lookup data is not available.",
                    )
                )
            else:
                try:
                    lookup_result = self.default_values_tool.lookup_by_cn_code(
                        cn_code=task_generation.calculation_input.cnCode,
                        year=task_generation.calculation_input.year,
                        country=task_generation.calculation_input.country,
                    )

                    task_results.append(
                        TaskResult(
                            name="default_value_lookup",
                            output=self._compact_cn_lookup(lookup_result),
                            success=True,
                        )
                    )

                except Exception as exc:
                    task_results.append(
                        TaskResult(
                            name="default_value_lookup",
                            output=None,
                            success=False,
                            error=str(exc),
                        )
                    )

        if not task_generation.calculation_input.cnCode and task_generation.product_name:
            task_results.append(
                TaskResult(
                    name="cn_code_lookup_note",
                    output={
                        "product_name": task_generation.product_name,
                        "message": "No CN code could be selected from the available lookup data.",
                    },
                    success=False,
                    error="CN code not found.",
                )
            )

        rag_outputs = []

        for query in task_generation.english_rag_queries:
            try:
                rag_outputs.append(
                    {
                        "query": query,
                        "results": [
                            self._compact_document(doc)
                            for doc in self.rag_tool.search_general(query=query, k=5)
                        ],
                    }
                )
            except Exception as exc:
                rag_outputs.append(
                    {
                        "query": query,
                        "error": str(exc),
                    }
                )

        task_results.append(
            TaskResult(
                name="rag_retrieval",
                output=rag_outputs,
                success=True,
            )
        )

        task_results.append(
            TaskResult(
                name="backend_calculation_explanation",
                output={
                    "executed": False,
                    "reason": "This agent only explains CBAM calculations and never calls backend calculation endpoints.",
                    "default_emissions_endpoint": "POST /api/cbam/default-emissions",
                    "actual_emissions_endpoint": "POST /api/cbam/actual-emissions",
                    "formulas": self._backend_calculation_formulas(),
                    "payload_shape": {
                        "country": task_generation.calculation_input.country,
                        "cnCode": task_generation.calculation_input.cnCode,
                        "year": task_generation.calculation_input.year,
                        "exportVolumeTons": task_generation.calculation_input.exportVolumeTons,
                    },
                },
                success=True,
            )
        )

        task_generation.can_calculate = False
        task_generation.question_to_user = None
        task_generation.missing_fields = []

        return {
            "task_generation": self._model_to_dict(task_generation),
            "task_results": [self._model_to_dict(item) for item in task_results],
        }

    @staticmethod
    def _backend_calculation_formulas() -> dict[str, Any]:
        return {
            "default_value_mode": {
                "when_to_use": "Use when actual factory activity data is unavailable and a default value exists for the country, CN code, and year.",
                "endpoint": "POST /api/cbam/default-emissions",
                "steps": [
                    "Look up the default emissions intensity by country, CN code, and year.",
                    "Select the year-specific default value in tCO2e per tonne.",
                    "Multiply exported tonnage by that selected default value.",
                ],
                "formulas": [
                    "embeddedEmissionsTco2e = exportVolumeTons x selectedDefaultValueTco2ePerTon",
                ],
                "source_files": [
                    "backend/src/main/java/com/carbonai/cbam/service/DefaultValueService.java",
                    "backend/README.md",
                ],
            },
            "actual_data_mode": {
                "when_to_use": "Use when actual facility activity data and emission factors are available.",
                "endpoint": "POST /api/cbam/actual-emissions",
                "steps": [
                    "For each activity, multiply activity amount by its emission factor to get kgCO2e.",
                    "Convert kgCO2e to tCO2e by dividing by 1000.",
                    "Classify electricity as indirect emissions and other activities as direct emissions.",
                    "Add direct and indirect emissions to get total facility emissions.",
                    "Divide total facility emissions by production volume to get specific emissions per tonne.",
                    "Multiply specific emissions by export volume to get exported embedded emissions.",
                ],
                "formulas": [
                    "activityEmissionsKg = amount x factorKgCo2ePerUnit",
                    "activityEmissionsTco2e = activityEmissionsKg / 1000",
                    "totalFacilityEmissionsTco2e = directEmissionsTco2e + indirectEmissionsTco2e",
                    "specificEmissionsTco2ePerTon = totalFacilityEmissionsTco2e / productionVolumeTons",
                    "exportedEmbeddedEmissionsTco2e = specificEmissionsTco2ePerTon x exportVolumeTons",
                ],
                "source_files": [
                    "backend/src/main/java/com/carbonai/cbam/service/ActualEmissionCalculationService.java",
                    "backend/README.md",
                ],
            },
            "report_validation": {
                "formula": "totalEmissionsTco2e = directEmissionsTco2e + indirectEmissionsTco2e",
                "source_files": [
                    "backend/src/main/java/com/carbonai/cbam/service/ReportValidationService.java",
                    "backend/README.md",
                ],
            },
        }

    def invoke(
        self,
        user_query: str,
        thread_id: str = "default",
    ) -> WorkflowResult:
        config = {
            "configurable": {
                "thread_id": thread_id,
            }
        }

        result = self.graph.invoke(
            {
                "messages": [HumanMessage(content=user_query)],
                "user_query": user_query,
                "route": None,
                "normal_answer": None,
                "task_generation": None,
                "task_results": [],
                "final_answer": None,
            },
            config=config,
        )

        return WorkflowResult(
            user_query=user_query,
            route=self._parse_route(result["route"]),
            normal_answer=result.get("normal_answer"),
            task_generation=self._parse_task_generation(result.get("task_generation")),
            task_results=[
                self._parse_task_result(item)
                for item in result.get("task_results", [])
            ],
            final_answer=result.get("final_answer"),
            chat_history=self._messages_to_dicts(result.get("messages", [])),
        )

    @staticmethod
    def _patch_task_generation_from_text(
        task_generation: TaskGenerationOutput,
        messages: list[AnyMessage],
    ) -> TaskGenerationOutput:
        import re

        text = "\n".join(str(message.content) for message in messages)
        latest_text = str(messages[-1].content) if messages else text

        if task_generation.calculation_input.exportVolumeTons is None:
            match = re.search(
                r"(\d+(?:[.,]\d+)?)\s*(?:metric\s*)?(?:tons|tonnes|ton|t)\b",
                text,
                flags=re.IGNORECASE,
            )
            if match:
                task_generation.calculation_input.exportVolumeTons = float(
                    match.group(1).replace(",", ".")
                )

        if not task_generation.calculation_input.cnCode:
            match = re.search(r"\b\d{8}\b", text)
            if match:
                task_generation.calculation_input.cnCode = match.group(0)

        if not task_generation.product_name:
            task_generation.product_name = (
                CBAMWorkflow._extract_product_name(latest_text)
                or CBAMWorkflow._extract_product_name(text)
            )

        if not task_generation.calculation_input.year:
            match = re.search(r"\b(2026|2027|2028)\b", text)
            if match:
                task_generation.calculation_input.year = int(match.group(1))

        if not task_generation.calculation_input.country:
            task_generation.calculation_input.country = "Turkey"

        task_generation.missing_fields = []
        task_generation.can_calculate = False
        task_generation.question_to_user = None

        return task_generation

    @staticmethod
    def _extract_product_name(text: str) -> str | None:
        import re

        patterns = [
            r"(?:cn\s*code|cn_code|cncode|commodity\s+code)\s+(?:for|of)\s+(.+)",
            r"(?:find|lookup|look\s+up|search)\s+(?:the\s+)?(?:cn\s*code|cn_code|cncode)\s+(?:for|of)\s+(.+)",
            r"(?:default\s+value|default\s+values)\s+(?:for|of)\s+(.+)",
            r"(?:how\s+about|what\s+about)\s+(?:the\s+)?(.+)",
        ]

        for pattern in patterns:
            match = re.search(pattern, text, flags=re.IGNORECASE)
            if not match:
                continue

            product_name = re.split(
                r"[?.!,;]|\b(?:in|from|for)\s+(?:turkey|türkiye|202[6-8])\b",
                match.group(1),
                maxsplit=1,
                flags=re.IGNORECASE,
            )[0]
            product_name = product_name.strip(" .,:;!?")

            if product_name:
                return product_name

        return None

    def _infer_product_name_from_lookup(self, user_query: str) -> str | None:
        if self.default_values_tool is None:
            return None

        try:
            matches = self.default_values_tool.search_by_description(
                query=user_query,
                limit=1,
            )
        except Exception:
            return None

        if not matches:
            return None

        return user_query

    @staticmethod
    def _build_default_values_tool() -> CBAMDefaultValuesTool | None:
        try:
            return CBAMDefaultValuesTool()
        except Exception:
            return None

    @staticmethod
    def _compact_document(doc) -> dict[str, Any]:
        metadata = getattr(doc, "metadata", {}) or {}
        page_content = str(getattr(doc, "page_content", "") or "")
        page_content = " ".join(page_content.split())

        return {
            "source_title": metadata.get("source_title", ""),
            "source_link": metadata.get("source_link", ""),
            "heading": metadata.get("heading_path", ""),
            "sector": metadata.get("sector", ""),
            "document_type": metadata.get("document_type", ""),
            "excerpt": page_content[:900],
        }

    @staticmethod
    def _compact_cn_lookup(rows: list[dict]) -> list[dict]:
        compact_rows = []

        for row in rows:
            compact_rows.append(
                {
                    "cn_code": row.get("cn_code") or row.get("cnCode"),
                    "description": row.get("description", ""),
                    "sector": row.get("sector", ""),
                    "country": row.get("country", ""),
                    "year": row.get("year"),
                    "source": row.get("source", ""),
                    "direct_default_tco2e_per_ton": row.get(
                        "direct_default_tco2e_per_ton"
                    ),
                    "indirect_default_tco2e_per_ton": row.get(
                        "indirect_default_tco2e_per_ton"
                    ),
                    "total_default_tco2e_per_ton": row.get(
                        "total_default_tco2e_per_ton"
                    ),
                    "selected_default_value_tco2e_per_ton": row.get(
                        "selected_default_value_tco2e_per_ton"
                    ),
                    "benchmark_column": row.get("benchmark_column"),
                    "production_route": row.get("production_route"),
                    "underlying_cbam_benchmark_route": row.get(
                        "underlying_cbam_benchmark_route"
                    ),
                }
            )

        return compact_rows

    @staticmethod
    def _missing_question(language: str, missing_fields: list[str]) -> str:
        if language == "tr":
            return "Hesaplama için lütfen eksik bilgileri paylaşın: " + ", ".join(
                missing_fields
            )

        return "Please provide the missing information: " + ", ".join(missing_fields)

    @staticmethod
    def _messages_to_dicts(messages: list[AnyMessage]) -> list[dict[str, str]]:
        history = []

        for message in messages:
            role = "assistant"

            if message.__class__.__name__ == "HumanMessage":
                role = "user"
            elif message.__class__.__name__ == "SystemMessage":
                role = "system"
            elif message.__class__.__name__ == "AIMessage":
                role = "assistant"

            history.append(
                {
                    "role": role,
                    "content": str(message.content),
                }
            )

        return history

    @staticmethod
    def _model_to_dict(model: Any) -> dict:
        if model is None:
            return {}

        if hasattr(model, "model_dump"):
            return model.model_dump()

        if hasattr(model, "dict"):
            return model.dict()

        return dict(model)

    @staticmethod
    def _parse_route(data: dict | None) -> RouteOutput:
        if data is None:
            return RouteOutput(query_type=QueryType.NORMAL, language="en")

        if hasattr(RouteOutput, "model_validate"):
            return RouteOutput.model_validate(data)

        return RouteOutput.parse_obj(data)

    @staticmethod
    def _parse_task_generation(data: dict | None) -> TaskGenerationOutput | None:
        if data is None:
            return None

        if hasattr(TaskGenerationOutput, "model_validate"):
            return TaskGenerationOutput.model_validate(data)

        return TaskGenerationOutput.parse_obj(data)

    @staticmethod
    def _parse_task_result(data: dict) -> TaskResult:
        if hasattr(TaskResult, "model_validate"):
            return TaskResult.model_validate(data)

        return TaskResult.parse_obj(data)
