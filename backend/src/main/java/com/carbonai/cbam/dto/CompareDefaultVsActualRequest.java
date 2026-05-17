package com.carbonai.cbam.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request body for /api/cbam/compare-default-vs-actual.
 *
 * Beginner-friendly explanation:
 * This request compares the cost impact of using fallback default emissions
 * versus measured actual emissions.
 *
 * Example request:
 * {
 *   "defaultSpecificEmissionsTco2ePerTon": 2.145,
 *   "actualSpecificEmissionsTco2ePerTon": 1.6,
 *   "exportVolumeTons": 100,
 *   "certificatePriceEurPerTco2e": 76
 * }
 */
public class CompareDefaultVsActualRequest {

    /** Default emissions intensity in tCO2e per ton. Example: 2.145. */
    @NotNull(message = "defaultSpecificEmissionsTco2ePerTon is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "defaultSpecificEmissionsTco2ePerTon must be >= 0")
    private BigDecimal defaultSpecificEmissionsTco2ePerTon;

    /** Actual emissions intensity in tCO2e per ton. Example: 1.6. */
    @NotNull(message = "actualSpecificEmissionsTco2ePerTon is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "actualSpecificEmissionsTco2ePerTon must be >= 0")
    private BigDecimal actualSpecificEmissionsTco2ePerTon;

    /** Export quantity in tons. Example: 100. */
    @NotNull(message = "exportVolumeTons is required")
    @DecimalMin(value = "0.0001", message = "exportVolumeTons must be > 0")
    private BigDecimal exportVolumeTons;

    /** Carbon price in EUR per tCO2e. Example: 76. */
    @NotNull(message = "certificatePriceEurPerTco2e is required")
    @DecimalMin(value = "0.0001", message = "certificatePriceEurPerTco2e must be > 0")
    private BigDecimal certificatePriceEurPerTco2e;

    public BigDecimal getDefaultSpecificEmissionsTco2ePerTon() {
        return defaultSpecificEmissionsTco2ePerTon;
    }

    public void setDefaultSpecificEmissionsTco2ePerTon(BigDecimal defaultSpecificEmissionsTco2ePerTon) {
        this.defaultSpecificEmissionsTco2ePerTon = defaultSpecificEmissionsTco2ePerTon;
    }

    public BigDecimal getActualSpecificEmissionsTco2ePerTon() {
        return actualSpecificEmissionsTco2ePerTon;
    }

    public void setActualSpecificEmissionsTco2ePerTon(BigDecimal actualSpecificEmissionsTco2ePerTon) {
        this.actualSpecificEmissionsTco2ePerTon = actualSpecificEmissionsTco2ePerTon;
    }

    public BigDecimal getExportVolumeTons() {
        return exportVolumeTons;
    }

    public void setExportVolumeTons(BigDecimal exportVolumeTons) {
        this.exportVolumeTons = exportVolumeTons;
    }

    public BigDecimal getCertificatePriceEurPerTco2e() {
        return certificatePriceEurPerTco2e;
    }

    public void setCertificatePriceEurPerTco2e(BigDecimal certificatePriceEurPerTco2e) {
        this.certificatePriceEurPerTco2e = certificatePriceEurPerTco2e;
    }
}
