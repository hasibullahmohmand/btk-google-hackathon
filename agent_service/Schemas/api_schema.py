from typing import Any, Optional

from pydantic import BaseModel, Field


class ChatRequest(BaseModel):
    message: str = Field(..., min_length=1)
    thread_id: str = "default"

    csv_uploaded: bool = False
    csv_file_path: Optional[str] = None


class ChatResponse(BaseModel):
    answer: str
    thread_id: str
    query_type: str
    language: str
    route: dict[str, Any]
    task_generation: Optional[dict[str, Any]] = None
    task_results: list[dict[str, Any]] = Field(default_factory=list)


class HealthResponse(BaseModel):
    status: str
    service: str