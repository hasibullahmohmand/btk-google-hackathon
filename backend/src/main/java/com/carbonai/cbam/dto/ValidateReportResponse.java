package com.carbonai.cbam.dto;

import com.carbonai.cbam.model.ValidationError;

import java.util.List;

/**
 * Response body for /api/cbam/validate-report.
 *
 * Beginner-friendly explanation:
 * This response tells the client whether the report passed validation and, if
 * not, exactly which rules failed.
 */
public class ValidateReportResponse {

    /** True when no validation rules failed. */
    private boolean valid;
    /** Detailed validation rule failures. */
    private List<ValidationError> errors;
    /** Non-blocking notes, if any are added in the future. */
    private List<String> warnings;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
}
