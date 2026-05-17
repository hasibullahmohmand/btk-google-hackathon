package com.carbonai.cbam.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request body for /api/cbam/simple-cost.
 *
 * Beginner-friendly explanation:
 * Use this request when the emissions amount is already known and only a quick
 * EUR exposure estimate is needed.
 *
 * Example request:
 * {
 *   "embeddedEmissionsTco2e": 214.5,
 *   "certificatePriceEurPerTco2e": 76
 * }
 */
public class SimpleCostRequest {

    /** Known embedded emissions quantity in tCO2e. Example: 214.5. */
    @NotNull(message = "embeddedEmissionsTco2e is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "embeddedEmissionsTco2e must be >= 0")
    private BigDecimal embeddedEmissionsTco2e;

    /** Carbon or certificate price in EUR per tCO2e. Example: 76. */
    @NotNull(message = "certificatePriceEurPerTco2e is required")
    @DecimalMin(value = "0.0001", message = "certificatePriceEurPerTco2e must be > 0")
    private BigDecimal certificatePriceEurPerTco2e;

    public BigDecimal getEmbeddedEmissionsTco2e() {
        return embeddedEmissionsTco2e;
    }

    public void setEmbeddedEmissionsTco2e(BigDecimal embeddedEmissionsTco2e) {
        this.embeddedEmissionsTco2e = embeddedEmissionsTco2e;
    }

    public BigDecimal getCertificatePriceEurPerTco2e() {
        return certificatePriceEurPerTco2e;
    }

    public void setCertificatePriceEurPerTco2e(BigDecimal certificatePriceEurPerTco2e) {
        this.certificatePriceEurPerTco2e = certificatePriceEurPerTco2e;
    }
}
