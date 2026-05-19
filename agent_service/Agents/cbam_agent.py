from langchain_core.messages import AIMessage
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder

from Agents.llm_output import normalize_llm_content
from Agents.workflow import CBAMWorkflow
from Agents.model_factory import build_chat_model
from Schemas.orchestration_schema import QueryType, WorkflowResult


class CBAMAgent:
    """
    Main chatbot agent.

    Responsibilities:
    - call the workflow
    - use workflow memory
    - write the final answer
    - append final CBAM answers back into LangGraph memory
    """

    def __init__(
        self,
        model_name: str = "llama3.2",
        cbam_model_name: str = "gemini:gemini-1.5-flash",
        temperature: float = 0.0,
    ):
        self.workflow = CBAMWorkflow(
            model_name=model_name,
            cbam_model_name=cbam_model_name,
            temperature=temperature,
        )

        self.llm = build_chat_model(model_name=cbam_model_name, temperature=temperature)

    def chat(self, message: str, thread_id: str = "default") -> dict:
        workflow_result = self.workflow.invoke(user_query=message, thread_id=thread_id)
        answer = self._write_final_answer(workflow_result)

        if workflow_result.route.query_type == QueryType.CBAM:
            self._append_ai_answer_to_memory(answer=answer, thread_id=thread_id)

        return {
            "answer": answer,
            "thread_id": thread_id,
            "query_type": workflow_result.route.query_type.value,
            "language": workflow_result.route.language,
            "route": self._model_to_dict(workflow_result.route),
            "task_generation": self._model_to_dict(workflow_result.task_generation),
            "task_results": [
                self._model_to_dict(task_result)
                for task_result in workflow_result.task_results
            ],
        }

    def _write_final_answer(self, workflow_result: WorkflowResult) -> str:
        route = workflow_result.route

        if route.query_type == QueryType.NORMAL:
            return workflow_result.normal_answer or "Hello! I am a CBAM assistant."

        writer_input = {
            "user_query": workflow_result.user_query,
            "route": self._model_to_dict(route),
            "task_generation": self._model_to_dict(workflow_result.task_generation),
            "task_results": [
                self._model_to_dict(task_result)
                for task_result in workflow_result.task_results
            ],
        }

        chat_history_messages = self._dicts_to_messages(workflow_result.chat_history)

        # Keep recent memory only. This is safer for llama3.2.
        chat_history_messages = chat_history_messages[-10:]


        writer_prompt = ChatPromptTemplate.from_messages([
            ("system", """
You are a multilingual CBAM chatbot and final answer writer.

You receive:
- chat history
- original user query
- orchestration result
- executed task results

Your job:
- write the final user-facing answer
- answer in the same language as the user
- be practical and clear
- use memory for follow-up questions
- explain which information was found
- explain missing information if something could not be calculated
- use default value lookup results only if they exist
- use RAG results only if they exist
- use calculation results only if the Java backend returned them
- do not invent CN codes
- do not invent emission factors
- do not invent legal references
- do not invent calculation results

For normal chat:
- return the normal_answer naturally.

For CBAM answers:
- summarize the useful tool results
- show default values when available
- show CN code, product description, country, year, sector, source, and selected value when available
- show formula or calculation method if RAG found it
- show calculated result if calculation succeeded
- if calculation failed because backend is unavailable, explain that the Java backend must be running
- if volume is missing, ask for export volume in tons
- if product or CN code is ambiguous, ask the user to choose the correct product or CN code

Keep the answer concise enough for a chatbot.
"""),
            MessagesPlaceholder("chat_history"),
            ("human", "{writer_input}"),
        ])

        writer_chain = writer_prompt | self.llm
        response = writer_chain.invoke(
            {
                "chat_history": chat_history_messages,
                "writer_input": str(writer_input),
            }
        )

        return normalize_llm_content(response.content)

    def _append_ai_answer_to_memory(self, answer: str, thread_id: str) -> None:
        config = {
            "configurable": {
                "thread_id": thread_id,
            }
        }

        current_state = self.workflow.graph.get_state(config)

        if not current_state or not current_state.values:
            return

        messages = current_state.values.get("messages", [])
        messages.append(AIMessage(content=answer))

        self.workflow.graph.update_state(
            config=config,
            values={
                "messages": messages,
            },
        )

    @staticmethod
    def _dicts_to_messages(history: list[dict[str, str]]):
        from langchain_core.messages import AIMessage, HumanMessage, SystemMessage

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
    def _model_to_dict(model):
        if hasattr(model, "model_dump"):
            return model.model_dump()

        if hasattr(model, "dict"):
            return model.dict()

        return dict(model)


if __name__ == "__main__":
    agent = CBAMAgent()

    result_1 = agent.chat(
        "What is the default value for aluminum chair from Turkey in 2026?",
        thread_id="demo-user",
    )
    print(result_1["answer"])

    result_2 = agent.chat(
        "Now calculate it for 10 tons.",
        thread_id="demo-user",
    )
    print(result_2["answer"])
