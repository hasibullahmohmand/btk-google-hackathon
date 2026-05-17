package com.carbonai.cbam.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * One activity input row for actual emissions calculations.
 *
 * Beginner-friendly explanation:
 * One row says "this much of this activity happened" and is later multiplied
 * by an emission factor to produce emissions.
 *
 * Example:
 * {
 *   "activityType": "NATURAL_GAS",
 *   "amount": 5000,
 *   "unit": "m3"
 * }
 */
public class ActivityInput {

    /** Activity category. Example: NATURAL_GAS, DIESEL, ELECTRICITY. */
    @NotBlank(message = "activityType is required")
    private String activityType;

    /** Physical amount used or consumed. Example: 5000. */
    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0001", message = "activity amount must be > 0")
    private BigDecimal amount;

    /** Unit of the physical amount. Example: m3, liter, kWh. */
    @NotBlank(message = "unit is required")
    private String unit;

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
