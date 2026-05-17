package com.carbonai.cbam.dto;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Standard error response returned by the global exception handler.
 *
 * Beginner-friendly explanation:
 * Whenever a request fails, the API returns this structure so the client can
 * understand the error code, the message, and any validation details.
 */
public class ErrorResponse {

    /** Machine-readable error code. Example: DEFAULT_VALUE_NOT_FOUND. */
    private String error;
    /** Human-readable summary of the problem. */
    private String message;
    /** Optional field-level details, mostly for validation errors. */
    private List<String> details;
    /** Time when the error response was created. */
    private OffsetDateTime timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(String error, String message, List<String> details, OffsetDateTime timestamp) {
        this.error = error;
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
