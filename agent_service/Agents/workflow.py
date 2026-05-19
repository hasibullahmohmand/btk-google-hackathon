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
    csv_uploaded: bool
    csv_file_path: str | None
    route: dict[str, Any] | None
    normal_answer: str | None
    task_generation: dict[str, Any] | None
    task_results: list[dict[str, Any]]
    final_answer: str | None


class CBAMWorkflow:
    def __init__(
        self,
        model_name: str = "llama3.2",
        cbam_model_name: str = "gemini:gemini-1.5-flash",
        temperature: float = 0.0,
    ):
        self.router = CBAMOrchestrator(model_name=model_name, temperature=temperature)
        self.normal_agent = NormalAgent(model_name=model_name, temperature=temperature)
        self.task_agent = CBAMTaskGenerationAgent(
            model_name=cbam_model_name,
            temperature=temperature,
        )

        self.rag_tool = CBAMRAGTool()
        self.default_values_tool = CBAMDefaultValuesTool()

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
            csv_uploaded=state.get("csv_uploaded", False),
            csv_file_path=state.get("csv_file_path"),
        )

        task_generation = self._patch_task_generation_from_text(
            task_generation=task_generation,
            messages=all_messages,
        )

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
                        output=lookup_result,
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

        missing_after_lookup = []

        if not task_generation.calculation_input.cnCode:
            missing_after_lookup.append("cnCode")

        if task_generation.calculation_input.exportVolumeTons is None:
            missing_after_lookup.append("exportVolumeTons")

        if task_generation.csv_uploaded and not task_generation.csv_file_path:
            missing_after_lookup.append("csv_file_path")

        if missing_after_lookup:
            question = self._missing_question(
                language=task_generation.language,
                missing_fields=missing_after_lookup,
            )

            task_results.append(
                TaskResult(
                    name="missing_information_after_lookup",
                    output={
                        "missing_fields": missing_after_lookup,
                        "question_to_user": question,
                    },
                    success=False,
                    error="Missing required information after lookup.",
                )
            )

            return {
                "task_generation": self._model_to_dict(task_generation),
                "task_results": [self._model_to_dict(item) for item in task_results],
                "final_answer": question,
                "messages": [AIMessage(content=question)],
            }

        try:
            calc_input = task_generation.calculation_input

            if task_generation.csv_uploaded:
                calculation_result = self.default_values_tool.calculate_actual_emissions(
                    cn_code=calc_input.cnCode,
                    export_volume_tons=calc_input.exportVolumeTons,
                    year=calc_input.year,
                    country=calc_input.country,
                    csv_file_path=task_generation.csv_file_path,
                )

                task_name = "actual_emissions_calculation"

            else:
                calculation_result = self.default_values_tool.calculate_default_emissions(
                    cn_code=calc_input.cnCode,
                    export_volume_tons=calc_input.exportVolumeTons,
                    year=calc_input.year,
                    country=calc_input.country,
                )

                task_name = "default_emissions_calculation"

            task_results.append(
                TaskResult(
                    name=task_name,
                    output=calculation_result,
                    success=True,
                )
            )

        except Exception as exc:
            task_results.append(
                TaskResult(
                    name="emissions_calculation",
                    output={
                        "csv_uploaded": task_generation.csv_uploaded,
                        "payload": {
                            "country": task_generation.calculation_input.country,
                            "cnCode": task_generation.calculation_input.cnCode,
                            "year": task_generation.calculation_input.year,
                            "exportVolumeTons": task_generation.calculation_input.exportVolumeTons,
                            "csvFilePath": task_generation.csv_file_path,
                        },
                    },
                    success=False,
                    error=str(exc),
                )
            )

        return {
            "task_generation": self._model_to_dict(task_generation),
            "task_results": [self._model_to_dict(item) for item in task_results],
        }

    def invoke(
        self,
        user_query: str,
        thread_id: str = "default",
        csv_uploaded: bool = False,
        csv_file_path: str | None = None,
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
                "csv_uploaded": csv_uploaded,
                "csv_file_path": csv_file_path,
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

        if not task_generation.calculation_input.year:
            match = re.search(r"\b(2026|2027|2028)\b", text)
            if match:
                task_generation.calculation_input.year = int(match.group(1))

        if not task_generation.calculation_input.country:
            task_generation.calculation_input.country = "Turkey"

        missing = []

        if not task_generation.calculation_input.cnCode and not task_generation.product_name:
            missing.append("cnCode or product_name")

        if task_generation.calculation_input.exportVolumeTons is None:
            missing.append("exportVolumeTons")

        if task_generation.csv_uploaded and not task_generation.csv_file_path:
            missing.append("csv_file_path")

        task_generation.missing_fields = missing

        task_generation.can_calculate = (
            not missing
            and task_generation.calculation_input.exportVolumeTons is not None
            and (
                task_generation.calculation_input.cnCode is not None
                or task_generation.product_name is not None
            )
        )

        if missing:
            if task_generation.language == "tr":
                task_generation.question_to_user = (
                    "Hesaplama için lütfen eksik bilgileri paylaşın: "
                    + ", ".join(missing)
                )
            else:
                task_generation.question_to_user = (
                    "Please provide the missing information: "
                    + ", ".join(missing)
                )
        else:
            task_generation.question_to_user = None

        return task_generation

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
