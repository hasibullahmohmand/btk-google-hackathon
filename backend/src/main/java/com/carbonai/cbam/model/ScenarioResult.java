package com.carbonai.cbam.model;

import java.math.BigDecimal;

/**
 * One carbon price scenario result.
 *
 * Source-of-truth explanation:
 * This model pairs one ETS-linked certificate price with the resulting cost.
 */
public class ScenarioResult {

    /** One tested ETS-linked price in EUR per tCO2e. */
    private BigDecimal euEtsWeeklyAveragePriceEurPerTco2e;
    /** Estimated EUR cost for the tested carbon price. */
    private BigDecimal estimatedCostEur;

    public BigDecimal getEuEtsWeeklyAveragePriceEurPerTco2e() {
        return euEtsWeeklyAveragePriceEurPerTco2e;
    }

    public void setEuEtsWeeklyAveragePriceEurPerTco2e(BigDecimal euEtsWeeklyAveragePriceEurPerTco2e) {
        this.euEtsWeeklyAveragePriceEurPerTco2e = euEtsWeeklyAveragePriceEurPerTco2e;
    }

    public BigDecimal getEstimatedCostEur() {
        return estimatedCostEur;
    }

    public void setEstimatedCostEur(BigDecimal estimatedCostEur) {
        this.estimatedCostEur = estimatedCostEur;
    }
}
