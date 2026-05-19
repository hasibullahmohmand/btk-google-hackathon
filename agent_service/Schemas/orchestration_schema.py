from enum import Enum
from typing import Any, Optional

from pydantic import BaseModel, Field


class QueryType(str, Enum):
    NORMAL = "normal"
    CBAM = "cbam"


class RouteOutput(BaseModel):
    query_type: QueryType = Field(..., description="normal or cbam")
    language: str = Field(..., description="Detected user language, for example en or tr.")


class NormalAnswer(BaseModel):
    language: str
    answer: str


class CBAMCalculationInput(BaseModel):
    country: str = Field(default="Turkey")
    cnCode: Optional[str] = None
    year: int = Field(default=2026)
    exportVolumeTons: Optional[float] = None


class TaskGenerationOutput(BaseModel):
    language: str
    product_name: Optional[str] = None

    calculation_input: CBAMCalculationInput = Field(
        default_factory=CBAMCalculationInput
    )

    english_rag_queries: list[str] = Field(default_factory=list)

    csv_uploaded: bool = False
    csv_file_path: Optional[str] = None

    missing_fields: list[str] = Field(default_factory=list)
    question_to_user: Optional[str] = None
    can_calculate: bool = False


class TaskResult(BaseModel):
    name: str
    output: Any = None
    success: bool = True
    error: Optional[str] = None


class WorkflowResult(BaseModel):
    user_query: str
    route: RouteOutput
    normal_answer: Optional[str] = None
    task_generation: Optional[TaskGenerationOutput] = None
    task_results: list[TaskResult] = Field(default_factory=list)
    final_answer: Optional[str] = None
    chat_history: list[dict[str, str]] = Field(default_factory=list)