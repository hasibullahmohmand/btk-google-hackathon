package com.carbonai.cbam.model;

import java.math.BigDecimal;

/**
 * One carbon price scenario result.
 *
 * Beginner-friendly explanation:
 * This model pairs one possible carbon price with the cost estimate calculated
 * from that price.
 */
public class ScenarioResult {

    /** One tested carbon price in EUR per tCO2e. */
    private BigDecimal priceEurPerTco2e;
    /** Estimated EUR cost for the tested carbon price. */
    private BigDecimal estimatedCostEur;

    public BigDecimal getPriceEurPerTco2e() {
        return priceEurPerTco2e;
    }

    public void setPriceEurPerTco2e(BigDecimal priceEurPerTco2e) {
        this.priceEurPerTco2e = priceEurPerTco2e;
    }

    public BigDecimal getEstimatedCostEur() {
        return estimatedCostEur;
    }

    public void setEstimatedCostEur(BigDecimal estimatedCostEur) {
        this.estimatedCostEur = estimatedCostEur;
    }
}
