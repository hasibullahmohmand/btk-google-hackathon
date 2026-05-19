import os
import shutil
from contextlib import asynccontextmanager
from pathlib import Path
from typing import Any

from fastapi import FastAPI, File, Form, HTTPException, UploadFile
from fastapi.middleware.cors import CORSMiddleware
from langchain_core.messages import AIMessage

from Agents.model_factory import load_env_file
from Agents.workflow import CBAMWorkflow
from Agents.writer_agent import CBAMWriterAgent
from Schemas.api_schema import ChatRequest, ChatResponse, HealthResponse
from Schemas.orchestration_schema import QueryType


UPLOAD_DIR = Path("uploads")

workflow: CBAMWorkflow | None = None
writer_agent: CBAMWriterAgent | None = None


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


@asynccontextmanager
async def lifespan(app: FastAPI):
    global workflow
    global writer_agent

    UPLOAD_DIR.mkdir(parents=True, exist_ok=True)
    load_env_file()

    model_name = os.getenv("CBAM_AGENT_MODEL", "llama3.2")
    cbam_model_name = os.getenv("CBAM_MODEL", "gemini:gemini-1.5-flash")
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
    if workflow is None or writer_agent is None:
        raise HTTPException(
            status_code=503,
            detail="CBAM services are not initialized.",
        )

    try:
        workflow_result = workflow.invoke(
            user_query=request.message,
            thread_id=request.thread_id,
            csv_uploaded=request.csv_uploaded,
            csv_file_path=request.csv_file_path,
        )

        answer = workflow_result.final_answer

        if not answer:
            answer = writer_agent.write(workflow_result)

        if workflow_result.route.query_type == QueryType.CBAM:
            append_ai_answer_to_memory(
                answer=answer,
                thread_id=request.thread_id,
            )

        return ChatResponse(
            answer=answer,
            thread_id=request.thread_id,
            query_type=workflow_result.route.query_type.value,
            language=workflow_result.route.language,
            route=model_to_dict(workflow_result.route) or {},
            task_generation=model_to_dict(workflow_result.task_generation),
            task_results=[
                model_to_dict(item) or {}
                for item in workflow_result.task_results
            ],
        )

    except Exception as exc:
        raise HTTPException(status_code=500, detail=str(exc)) from exc


@app.post("/api/chat/upload-csv", response_model=ChatResponse)
async def chat_with_csv(
    message: str = Form(...),
    thread_id: str = Form("default"),
    file: UploadFile = File(...),
):
    if workflow is None or writer_agent is None:
        raise HTTPException(
            status_code=503,
            detail="CBAM services are not initialized.",
        )

    if not file.filename:
        raise HTTPException(status_code=400, detail="CSV file is required.")

    safe_filename = Path(file.filename).name
    file_path = UPLOAD_DIR / safe_filename

    with file_path.open("wb") as buffer:
        shutil.copyfileobj(file.file, buffer)

    try:
        workflow_result = workflow.invoke(
            user_query=message,
            thread_id=thread_id,
            csv_uploaded=True,
            csv_file_path=str(file_path),
        )

        answer = workflow_result.final_answer

        if not answer:
            answer = writer_agent.write(workflow_result)

        if workflow_result.route.query_type == QueryType.CBAM:
            append_ai_answer_to_memory(
                answer=answer,
                thread_id=thread_id,
            )

        return ChatResponse(
            answer=answer,
            thread_id=thread_id,
            query_type=workflow_result.route.query_type.value,
            language=workflow_result.route.language,
            route=model_to_dict(workflow_result.route) or {},
            task_generation=model_to_dict(workflow_result.task_generation),
            task_results=[
                model_to_dict(item) or {}
                for item in workflow_result.task_results
            ],
        )

    except Exception as exc:
        raise HTTPException(status_code=500, detail=str(exc)) from exc
