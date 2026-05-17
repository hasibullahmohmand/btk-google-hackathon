package com.carbonai.cbam.dto;

import com.carbonai.cbam.model.ScenarioResult;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response body for /api/cbam/scenarios.
 *
 * Beginner-friendly explanation:
 * This response returns one estimated EUR cost for each price scenario.
 *
 * Example response:
 * {
 *   "embeddedEmissionsTco2e": 214.5,
 *   "scenarios": [
 *     {
 *       "priceEurPerTco2e": 76,
 *       "estimatedCostEur": 16302
 *     }
 *   ]
 * }
 */
public class CarbonPriceScenariosResponse {

    /** Embedded emissions quantity used for all scenarios. */
    private BigDecimal embeddedEmissionsTco2e;
    /** List of calculated scenario results. */
    private List<ScenarioResult> scenarios;

    public BigDecimal getEmbeddedEmissionsTco2e() {
        return embeddedEmissionsTco2e;
    }

    public void setEmbeddedEmissionsTco2e(BigDecimal embeddedEmissionsTco2e) {
        this.embeddedEmissionsTco2e = embeddedEmissionsTco2e;
    }

    public List<ScenarioResult> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<ScenarioResult> scenarios) {
        this.scenarios = scenarios;
    }
}
