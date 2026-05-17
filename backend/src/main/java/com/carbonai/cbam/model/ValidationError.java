package com.carbonai.cbam.model;

import java.math.BigDecimal;

/**
 * One validation rule failure for report validation.
 *
 * Beginner-friendly explanation:
 * This model explains which validation rule failed and, when relevant, what
 * value was expected versus what value was actually provided.
 */
public class ValidationError {

    /** Validation rule code. Example: R0010. */
    private String code;
    /** Human-readable description of the failed rule. */
    private String message;
    /** Expected value when comparison is possible. */
    private BigDecimal expectedValue;
    /** Actual provided value when comparison is possible. */
    private BigDecimal actualValue;

    public ValidationError() {
    }

    public ValidationError(String code, String message, BigDecimal expectedValue, BigDecimal actualValue) {
        this.code = code;
        this.message = message;
        this.expectedValue = expectedValue;
        this.actualValue = actualValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(BigDecimal expectedValue) {
        this.expectedValue = expectedValue;
    }

    public BigDecimal getActualValue() {
        return actualValue;
    }

    public void setActualValue(BigDecimal actualValue) {
        this.actualValue = actualValue;
    }
}
