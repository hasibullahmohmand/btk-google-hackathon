package com.carbonai.cbam.dto;

import java.math.BigDecimal;

/**
 * Response body for /api/cbam/simple-cost.
 *
 * Beginner-friendly explanation:
 * This response shows the emissions amount, the selected price, and the
 * estimated financial exposure in EUR.
 */
public class SimpleCostResponse {

    /** Embedded emissions input in tCO2e. */
    private BigDecimal embeddedEmissionsTco2e;
    /** Applied certificate price in EUR per tCO2e. */
    private BigDecimal certificatePriceEurPerTco2e;
    /** Estimated cost in EUR. */
    private BigDecimal estimatedCostEur;
    /** Human-readable formula string. */
    private String formula;

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

    public BigDecimal getEstimatedCostEur() {
        return estimatedCostEur;
    }

    public void setEstimatedCostEur(BigDecimal estimatedCostEur) {
        this.estimatedCostEur = estimatedCostEur;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
