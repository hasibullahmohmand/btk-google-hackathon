package com.carbonai.cbam.dto;

import java.math.BigDecimal;

/**
 * Response body for /api/cbam/compare-default-vs-actual.
 *
 * Beginner-friendly explanation:
 * This response shows the cost of the default-value path, the cost of the
 * actual-data path, and the possible savings from using actual data.
 */
public class CompareDefaultVsActualResponse {

    /** Cost estimate using the default emissions intensity. */
    private BigDecimal defaultCostEur;
    /** Cost estimate using the actual emissions intensity. */
    private BigDecimal actualCostEur;
    /** Potential EUR savings from using actual data. */
    private BigDecimal potentialSavingsEur;
    /** Savings expressed as a percentage of the default cost. */
    private BigDecimal savingsPercent;
    /** Human-readable business interpretation of the result. */
    private String message;

    public BigDecimal getDefaultCostEur() {
        return defaultCostEur;
    }

    public void setDefaultCostEur(BigDecimal defaultCostEur) {
        this.defaultCostEur = defaultCostEur;
    }

    public BigDecimal getActualCostEur() {
        return actualCostEur;
    }

    public void setActualCostEur(BigDecimal actualCostEur) {
        this.actualCostEur = actualCostEur;
    }

    public BigDecimal getPotentialSavingsEur() {
        return potentialSavingsEur;
    }

    public void setPotentialSavingsEur(BigDecimal potentialSavingsEur) {
        this.potentialSavingsEur = potentialSavingsEur;
    }

    public BigDecimal getSavingsPercent() {
        return savingsPercent;
    }

    public void setSavingsPercent(BigDecimal savingsPercent) {
        this.savingsPercent = savingsPercent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
