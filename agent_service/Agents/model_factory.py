from __future__ import annotations

import os
from pathlib import Path


def load_env_file(path: str | Path = ".env") -> None:
    env_path = _resolve_env_path(path)
    if env_path is None:
        return

    for line in env_path.read_text(encoding="utf-8").splitlines():
        line = line.strip()
        if not line or line.startswith("#") or "=" not in line:
            continue

        key, value = line.split("=", 1)
        key = key.strip()
        value = value.strip().strip('"').strip("'")
        if value and not os.environ.get(key):
            os.environ[key] = value


def _resolve_env_path(path: str | Path) -> Path | None:
    requested_path = Path(path)
    if requested_path.is_absolute():
        return requested_path if requested_path.exists() else None

    candidates = [
        Path.cwd() / requested_path,
        Path(__file__).resolve().parents[2] / requested_path,
        Path(__file__).resolve().parents[3] / requested_path,
    ]

    for candidate in candidates:
        if candidate.exists():
            return candidate

    return None


def build_chat_model(model_name: str, temperature: float = 0.0):
    provider, model = _split_model_name(model_name)

    if provider == "gemini":
        return _build_gemini_model(model=model, temperature=temperature)

    from langchain_ollama import ChatOllama

    return ChatOllama(model=model, temperature=temperature)


def _split_model_name(model_name: str) -> tuple[str, str]:
    if ":" not in model_name:
        return "ollama", model_name

    provider, model = model_name.split(":", 1)
    return provider.strip().lower(), model.strip()


def _build_gemini_model(model: str, temperature: float):
    load_env_file()

    api_key = os.getenv("GOOGLE_API_KEY") or os.getenv("GEMINI_API_KEY")
    if not api_key:
        raise RuntimeError(
            "Gemini model requested, but GOOGLE_API_KEY/GEMINI_API_KEY is missing. "
            "Add it to .env first."
        )

    try:
        from langchain_google_genai import ChatGoogleGenerativeAI
    except ImportError as exc:
        raise RuntimeError(
            "Gemini model requested, but langchain-google-genai is not installed. "
            "Install it with: pip install langchain-google-genai"
        ) from exc

    return ChatGoogleGenerativeAI(
        model=model,
        temperature=temperature,
        google_api_key=api_key,
    )
