package com.carbonai.cbam.model;

import java.math.BigDecimal;

/**
 * Detailed emissions result for one activity input row.
 *
 * Beginner-friendly explanation:
 * This object shows how a single activity row was translated into emissions.
 */
public class ActivityEmissionBreakdown {

    /** Activity category that was calculated. */
    private String activityType;
    /** Physical input amount. */
    private BigDecimal amount;
    /** Unit of the physical amount. */
    private String unit;
    /** Emission factor used for the calculation. */
    private BigDecimal factor;
    /** Unit of the emission factor. Example: kgCO2e/t. */
    private String factorUnit;
    /** Calculated emissions for this row in tCO2e. */
    private BigDecimal emissionsTco2e;
    /** DIRECT for fuels, INDIRECT for electricity in this demo logic. */
    private String emissionCategory;

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

    public BigDecimal getFactor() {
        return factor;
    }

    public void setFactor(BigDecimal factor) {
        this.factor = factor;
    }

    public String getFactorUnit() {
        return factorUnit;
    }

    public void setFactorUnit(String factorUnit) {
        this.factorUnit = factorUnit;
    }

    public BigDecimal getEmissionsTco2e() {
        return emissionsTco2e;
    }

    public void setEmissionsTco2e(BigDecimal emissionsTco2e) {
        this.emissionsTco2e = emissionsTco2e;
    }

    public String getEmissionCategory() {
        return emissionCategory;
    }

    public void setEmissionCategory(String emissionCategory) {
        this.emissionCategory = emissionCategory;
    }
}
