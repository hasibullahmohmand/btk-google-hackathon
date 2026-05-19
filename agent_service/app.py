import os
import sys
import uuid
from contextlib import asynccontextmanager
from pathlib import Path
from typing import Any

SERVICE_DIR = Path(__file__).resolve().parent
if str(SERVICE_DIR) not in sys.path:
    sys.path.insert(0, str(SERVICE_DIR))

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import PlainTextResponse
from langchain_core.messages import AIMessage

from Agents.model_factory import load_env_file
from Agents.workflow import CBAMWorkflow
from Agents.writer_agent import CBAMWriterAgent
from Schemas.api_schema import ChatRequest, ChatResponse, HealthResponse
from Schemas.orchestration_schema import QueryType


workflow: CBAMWorkflow | None = None
writer_agent: CBAMWriterAgent | None = None

try:
    import gradio as gr
except ImportError:
    gr = None


def model_to_dict(model: Any) -> dict | None:
    if model is None:
        return None

    if hasattr(model, "model_dump"):
        return model.model_dump()

    if hasattr(model, "dict"):
        return model.dict()

    return dict(model)


def append_ai_answer_to_memory(answer: str, thread_id: str) -> None:
    if workflow is None:
        return

    config = {
        "configurable": {
            "thread_id": thread_id,
        }
    }

    current_state = workflow.graph.get_state(config)

    if not current_state or not current_state.values:
        return

    messages = current_state.values.get("messages", [])
    messages.append(AIMessage(content=answer))

    workflow.graph.update_state(
        config=config,
        values={"messages": messages},
    )


def resolve_thread_id(thread_id: str | None = None) -> str:
    candidate = str(thread_id or "").strip()

    if candidate and candidate != "default":
        return candidate

    return f"thread-{uuid.uuid4().hex}"


def run_agent_chat(
    message: str,
    thread_id: str | None = None,
) -> ChatResponse:
    if workflow is None or writer_agent is None:
        raise RuntimeError("CBAM services are not initialized.")

    resolved_thread_id = resolve_thread_id(thread_id)

    workflow_result = workflow.invoke(
        user_query=message,
        thread_id=resolved_thread_id,
    )

    answer = workflow_result.final_answer

    if not answer:
        answer = writer_agent.write(workflow_result)

    if workflow_result.route.query_type == QueryType.CBAM:
        append_ai_answer_to_memory(
            answer=answer,
            thread_id=resolved_thread_id,
        )

    return ChatResponse(
        answer=answer,
        thread_id=resolved_thread_id,
        query_type=workflow_result.route.query_type.value,
        language=workflow_result.route.language,
        route=model_to_dict(workflow_result.route) or {},
        task_generation=model_to_dict(workflow_result.task_generation),
        task_results=[
            model_to_dict(item) or {}
            for item in workflow_result.task_results
        ],
    )


def create_gradio_demo():
    if gr is None:
        return None

    def normalize_chat_history(history):
        normalized = []

        for item in history or []:
            if isinstance(item, dict):
                role = item.get("role")
                content = item.get("content")

                if role and content is not None:
                    normalized.append(
                        {
                            "role": role,
                            "content": str(content),
                        }
                    )

                continue

            if isinstance(item, (list, tuple)) and len(item) == 2:
                user_message, assistant_message = item

                if user_message:
                    normalized.append(
                        {
                            "role": "user",
                            "content": str(user_message),
                        }
                    )

                if assistant_message:
                    normalized.append(
                        {
                            "role": "assistant",
                            "content": str(assistant_message),
                        }
                    )

        return normalized

    def submit(message, thread_id, history):
        history = normalize_chat_history(history)
        display_thread_id = str(thread_id or "").strip()

        if not message or not message.strip():
            return history, "", display_thread_id, None

        try:
            response = run_agent_chat(
                message=message.strip(),
                thread_id=display_thread_id or None,
            )
        except Exception as exc:
            history.extend(
                [
                    {
                        "role": "user",
                        "content": message,
                    },
                    {
                        "role": "assistant",
                        "content": f"Error: {exc}",
                    },
                ]
            )
            return history, "", display_thread_id, {"error": str(exc)}

        details = (
            response.model_dump()
            if hasattr(response, "model_dump")
            else response.dict()
        )
        history.extend(
            [
                {
                    "role": "user",
                    "content": message,
                },
                {
                    "role": "assistant",
                    "content": response.answer,
                },
            ]
        )
        return history, "", response.thread_id, details

    with gr.Blocks(title="CBAM Agent Test UI") as demo:
        gr.Markdown("# CBAM Agent Test UI")
        gr.Markdown("Use this page to test the local agent service.")

        chatbot = gr.Chatbot(label="Conversation")

        with gr.Row():
            thread_id = gr.Textbox(
                value="",
                label="Thread ID",
                placeholder="Auto-generated on first message",
                scale=1,
            )

        message = gr.Textbox(
            label="Message",
            placeholder="Ask a CBAM question...",
            lines=3,
        )

        with gr.Row():
            submit_button = gr.Button("Send", variant="primary")
            gr.ClearButton(
                [message, chatbot],
                value="Clear",
            )

        details = gr.JSON(label="Response details")

        submit_button.click(
            fn=submit,
            inputs=[message, thread_id, chatbot],
            outputs=[chatbot, message, thread_id, details],
        )
        message.submit(
            fn=submit,
            inputs=[message, thread_id, chatbot],
            outputs=[chatbot, message, thread_id, details],
        )

    return demo


@asynccontextmanager
async def lifespan(app: FastAPI):
    global workflow
    global writer_agent

    load_env_file()

    model_name = os.getenv("CBAM_AGENT_MODEL", "llama3.2")
    cbam_model_name = os.getenv("CBAM_MODEL", "gemini:gemini-2.5-flash")
    writer_model_name = os.getenv("CBAM_WRITER_MODEL", cbam_model_name)

    workflow = CBAMWorkflow(
        model_name=model_name,
        cbam_model_name=cbam_model_name,
        temperature=0.0,
    )

    writer_agent = CBAMWriterAgent(
        model_name=writer_model_name,
        temperature=0.0,
    )

    yield

    workflow = None
    writer_agent = None


app = FastAPI(
    title="CBAM Agent API",
    version="0.1.0",
    lifespan=lifespan,
)


app.add_middleware(
    CORSMiddleware,
    allow_origins=os.getenv("CORS_ALLOW_ORIGINS", "*").split(","),
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/health", response_model=HealthResponse)
def health():
    return HealthResponse(
        status="ok",
        service="cbam-agent-api",
    )


@app.post("/api/chat", response_model=ChatResponse)
def chat(request: ChatRequest):
    try:
        return run_agent_chat(
            message=request.message,
            thread_id=request.thread_id,
        )
    except RuntimeError as exc:
        raise HTTPException(status_code=503, detail=str(exc)) from exc
    except Exception as exc:
        raise HTTPException(status_code=500, detail=str(exc)) from exc

if gr is None:
    @app.get("/gradio", response_class=PlainTextResponse)
    def gradio_missing():
        return "Gradio is not installed. Install it with: pip install gradio"
else:
    app = gr.mount_gradio_app(
        app,
        create_gradio_demo(),
        path="/gradio",
    )
