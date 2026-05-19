from __future__ import annotations

from typing import Any


EMPTY_LLM_TEXT_FALLBACK = "I could not extract a text answer from the model response."


def normalize_llm_content(content: Any) -> str:
    if isinstance(content, str):
        return content

    if isinstance(content, list):
        text_parts = []
        for block in content:
            if isinstance(block, dict):
                if block.get("type") == "text" and block.get("text"):
                    text_parts.append(str(block["text"]))
                continue

            if isinstance(block, str):
                text_parts.append(block)

        if text_parts:
            return "\n".join(text_parts)

        return EMPTY_LLM_TEXT_FALLBACK

    if content is None:
        return EMPTY_LLM_TEXT_FALLBACK

    return str(content)
