package com.carbonai.cbam.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request body for /api/cbam/scenarios.
 *
 * Beginner-friendly explanation:
 * This request asks the backend to keep emissions fixed and test several
 * possible carbon prices.
 *
 * Example request:
 * {
 *   "embeddedEmissionsTco2e": 214.5,
 *   "pricesEurPerTco2e": [76, 100, 120]
 * }
 */
public class CarbonPriceScenariosRequest {

    /** Embedded emissions quantity in tCO2e. Example: 214.5. */
    @NotNull(message = "embeddedEmissionsTco2e is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "embeddedEmissionsTco2e must be >= 0")
    private BigDecimal embeddedEmissionsTco2e;

    /** List of price scenarios in EUR per tCO2e. Example: [76, 100, 120]. */
    @NotEmpty(message = "pricesEurPerTco2e cannot be empty")
    private List<@DecimalMin(value = "0.0001", message = "each price must be > 0") BigDecimal> pricesEurPerTco2e;

    public BigDecimal getEmbeddedEmissionsTco2e() {
        return embeddedEmissionsTco2e;
    }

    public void setEmbeddedEmissionsTco2e(BigDecimal embeddedEmissionsTco2e) {
        this.embeddedEmissionsTco2e = embeddedEmissionsTco2e;
    }

    public List<BigDecimal> getPricesEurPerTco2e() {
        return pricesEurPerTco2e;
    }

    public void setPricesEurPerTco2e(List<BigDecimal> pricesEurPerTco2e) {
        this.pricesEurPerTco2e = pricesEurPerTco2e;
    }
}
