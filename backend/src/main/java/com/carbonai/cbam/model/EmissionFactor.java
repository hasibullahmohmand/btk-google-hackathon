package com.carbonai.cbam.model;

import java.math.BigDecimal;

/**
 * Seeded emission factor used for actual activity calculations.
 *
 * Beginner-friendly explanation:
 * An emission factor tells the system how much CO2e is associated with one unit
 * of a given activity, such as one liter of diesel or one kWh of electricity.
 */
public class EmissionFactor {

    /** Activity category. Example: DIESEL. */
    private String activityType;
    /** Unit expected for the activity amount. Example: liter. */
    private String unit;
    /** Numeric factor in kgCO2e per unit. */
    private BigDecimal factorKgCo2ePerUnit;
    /** Human-readable factor unit. Example: kgCO2e/liter. */
    private String factorUnit;
    /** Short explanation of where the factor came from. */
    private String source;

    public EmissionFactor() {
    }

    public EmissionFactor(String activityType, String unit, BigDecimal factorKgCo2ePerUnit, String factorUnit, String source) {
        this.activityType = activityType;
        this.unit = unit;
        this.factorKgCo2ePerUnit = factorKgCo2ePerUnit;
        this.factorUnit = factorUnit;
        this.source = source;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getFactorKgCo2ePerUnit() {
        return factorKgCo2ePerUnit;
    }

    public void setFactorKgCo2ePerUnit(BigDecimal factorKgCo2ePerUnit) {
        this.factorKgCo2ePerUnit = factorKgCo2ePerUnit;
    }

    public String getFactorUnit() {
        return factorUnit;
    }

    public void setFactorUnit(String factorUnit) {
        this.factorUnit = factorUnit;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
