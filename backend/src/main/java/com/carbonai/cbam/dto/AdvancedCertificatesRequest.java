package com.carbonai.cbam.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request body for /api/cbam/advanced-certificates.
 *
 * CBAM source-of-truth alignment:
 * This request follows the structure described in the raw markdown sources:
 * - certificates correspond to embedded emissions;
 * - the obligation is adjusted for free allocation under the EU ETS;
 * - the number of certificates can then be reduced for the carbon price
 *   effectively paid in the country of origin;
 * - the monetary exposure uses the ETS-linked certificate price.
 *
 * Example request:
 * {
 *   "actualSpecificEmbeddedEmissionsTco2ePerTon": 2.145,
 *   "specificEmbeddedFreeAllocationTco2ePerTon": 1.4625,
 *   "effectiveCarbonPricePaidInCountryOfOriginEurPerTco2e": 0,
 *   "euEtsWeeklyAveragePriceEurPerTco2e": 76,
 *   "importedQuantityTons": 100
 * }
 */
public class AdvancedCertificatesRequest {

    /** Specific embedded emissions of the imported good, in tCO2e per tonne. */
    @NotNull(message = "actualSpecificEmbeddedEmissionsTco2ePerTon is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "actualSpecificEmbeddedEmissionsTco2ePerTon must be >= 0")
    private BigDecimal actualSpecificEmbeddedEmissionsTco2ePerTon;

    /** Specific embedded free allocation adjustment (SEFA-aligned), in tCO2e per tonne. */
    @NotNull(message = "specificEmbeddedFreeAllocationTco2ePerTon is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "specificEmbeddedFreeAllocationTco2ePerTon must be >= 0")
    private BigDecimal specificEmbeddedFreeAllocationTco2ePerTon;

    /** Carbon price effectively paid in the country of origin, in EUR per tCO2e. */
    @NotNull(message = "effectiveCarbonPricePaidInCountryOfOriginEurPerTco2e is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "effectiveCarbonPricePaidInCountryOfOriginEurPerTco2e must be >= 0")
    private BigDecimal effectiveCarbonPricePaidInCountryOfOriginEurPerTco2e;

    /** ETS-linked price used for CBAM certificates, in EUR per tCO2e. */
    @NotNull(message = "euEtsWeeklyAveragePriceEurPerTco2e is required")
    @DecimalMin(value = "0.0001", message = "euEtsWeeklyAveragePriceEurPerTco2e must be > 0")
    private BigDecimal euEtsWeeklyAveragePriceEurPerTco2e;

    /** Imported quantity in tons. Example: 100. */
    @NotNull(message = "importedQuantityTons is required")
    @DecimalMin(value = "0.0001", message = "importedQuantityTons must be > 0")
    private BigDecimal importedQuantityTons;

    public BigDecimal getActualSpecificEmbeddedEmissionsTco2ePerTon() {
        return actualSpecificEmbeddedEmissionsTco2ePerTon;
    }

    public void setActualSpecificEmbeddedEmissionsTco2ePerTon(BigDecimal actualSpecificEmbeddedEmissionsTco2ePerTon) {
        this.actualSpecificEmbeddedEmissionsTco2ePerTon = actualSpecificEmbeddedEmissionsTco2ePerTon;
    }

    public BigDecimal getSpecificEmbeddedFreeAllocationTco2ePerTon() {
        return specificEmbeddedFreeAllocationTco2ePerTon;
    }

    public void setSpecificEmbeddedFreeAllocationTco2ePerTon(BigDecimal specificEmbeddedFreeAllocationTco2ePerTon) {
        this.specificEmbeddedFreeAllocationTco2ePerTon = specificEmbeddedFreeAllocationTco2ePerTon;
    }

    public BigDecimal getEffectiveCarbonPricePaidInCountryOfOriginEurPerTco2e() {
        return effectiveCarbonPricePaidInCountryOfOriginEurPerTco2e;
    }

    public void setEffectiveCarbonPricePaidInCountryOfOriginEurPerTco2e(BigDecimal effectiveCarbonPricePaidInCountryOfOriginEurPerTco2e) {
        this.effectiveCarbonPricePaidInCountryOfOriginEurPerTco2e = effectiveCarbonPricePaidInCountryOfOriginEurPerTco2e;
    }

    public BigDecimal getEuEtsWeeklyAveragePriceEurPerTco2e() {
        return euEtsWeeklyAveragePriceEurPerTco2e;
    }

    public void setEuEtsWeeklyAveragePriceEurPerTco2e(BigDecimal euEtsWeeklyAveragePriceEurPerTco2e) {
        this.euEtsWeeklyAveragePriceEurPerTco2e = euEtsWeeklyAveragePriceEurPerTco2e;
    }

    public BigDecimal getImportedQuantityTons() {
        return importedQuantityTons;
    }

    public void setImportedQuantityTons(BigDecimal importedQuantityTons) {
        this.importedQuantityTons = importedQuantityTons;
    }
}
