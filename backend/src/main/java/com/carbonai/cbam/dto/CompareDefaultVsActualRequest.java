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
 *   "defaultSpecificEmbeddedEmissionsTco2ePerTon": 2.145,
 *   "actualSpecificEmbeddedEmissionsTco2ePerTon": 1.6,
 *   "exportVolumeTons": 100,
 *   "euEtsWeeklyAveragePriceEurPerTco2e": 76
 * }
 */
public class CompareDefaultVsActualRequest {

    /** Default emissions intensity in tCO2e per ton. Example: 2.145. */
    @NotNull(message = "defaultSpecificEmbeddedEmissionsTco2ePerTon is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "defaultSpecificEmbeddedEmissionsTco2ePerTon must be >= 0")
    private BigDecimal defaultSpecificEmbeddedEmissionsTco2ePerTon;

    /** Actual emissions intensity in tCO2e per ton. Example: 1.6. */
    @NotNull(message = "actualSpecificEmbeddedEmissionsTco2ePerTon is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "actualSpecificEmbeddedEmissionsTco2ePerTon must be >= 0")
    private BigDecimal actualSpecificEmbeddedEmissionsTco2ePerTon;

    /** Export quantity in tons. Example: 100. */
    @NotNull(message = "exportVolumeTons is required")
    @DecimalMin(value = "0.0001", message = "exportVolumeTons must be > 0")
    private BigDecimal exportVolumeTons;

    /** ETS-linked certificate price in EUR per tCO2e. Example: 76. */
    @NotNull(message = "euEtsWeeklyAveragePriceEurPerTco2e is required")
    @DecimalMin(value = "0.0001", message = "euEtsWeeklyAveragePriceEurPerTco2e must be > 0")
    private BigDecimal euEtsWeeklyAveragePriceEurPerTco2e;

    public BigDecimal getDefaultSpecificEmbeddedEmissionsTco2ePerTon() {
        return defaultSpecificEmbeddedEmissionsTco2ePerTon;
    }

    public void setDefaultSpecificEmbeddedEmissionsTco2ePerTon(BigDecimal defaultSpecificEmbeddedEmissionsTco2ePerTon) {
        this.defaultSpecificEmbeddedEmissionsTco2ePerTon = defaultSpecificEmbeddedEmissionsTco2ePerTon;
    }

    public BigDecimal getActualSpecificEmbeddedEmissionsTco2ePerTon() {
        return actualSpecificEmbeddedEmissionsTco2ePerTon;
    }

    public void setActualSpecificEmbeddedEmissionsTco2ePerTon(BigDecimal actualSpecificEmbeddedEmissionsTco2ePerTon) {
        this.actualSpecificEmbeddedEmissionsTco2ePerTon = actualSpecificEmbeddedEmissionsTco2ePerTon;
    }

    public BigDecimal getExportVolumeTons() {
        return exportVolumeTons;
    }

    public void setExportVolumeTons(BigDecimal exportVolumeTons) {
        this.exportVolumeTons = exportVolumeTons;
    }

    public BigDecimal getEuEtsWeeklyAveragePriceEurPerTco2e() {
        return euEtsWeeklyAveragePriceEurPerTco2e;
    }

    public void setEuEtsWeeklyAveragePriceEurPerTco2e(BigDecimal euEtsWeeklyAveragePriceEurPerTco2e) {
        this.euEtsWeeklyAveragePriceEurPerTco2e = euEtsWeeklyAveragePriceEurPerTco2e;
    }
}
