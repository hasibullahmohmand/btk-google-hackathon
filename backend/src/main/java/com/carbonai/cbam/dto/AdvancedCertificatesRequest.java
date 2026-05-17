package com.carbonai.cbam.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request body for /api/cbam/advanced-certificates.
 *
 * Beginner-friendly explanation:
 * This request is used for the more detailed certificate formula where the
 * user provides actual specific emissions plus benchmark and deduction inputs.
 *
 * Example request:
 * {
 *   "actualSpecificEmissionsTco2ePerTon": 2.145,
 *   "cbamBenchmarkTco2ePerTon": 1.5,
 *   "cbamFactor": 0.975,
 *   "thirdCountryCarbonPriceEurPerTco2e": 0,
 *   "cbamCertificatePriceEurPerTco2e": 76,
 *   "importedQuantityTons": 100
 * }
 */
public class AdvancedCertificatesRequest {

    /** A in the formula: actual specific emissions in tCO2e per ton. Example: 2.145. */
    @NotNull(message = "actualSpecificEmissionsTco2ePerTon is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "actualSpecificEmissionsTco2ePerTon must be >= 0")
    private BigDecimal actualSpecificEmissionsTco2ePerTon;

    /** Benchmark used to calculate the free allowance deduction. Example: 1.5. */
    @NotNull(message = "cbamBenchmarkTco2ePerTon is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "cbamBenchmarkTco2ePerTon must be >= 0")
    private BigDecimal cbamBenchmarkTco2ePerTon;

    /** Free allowance factor between 0 and 1. Example: 0.975. */
    @NotNull(message = "cbamFactor is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "cbamFactor must be between 0 and 1")
    @DecimalMax(value = "1.0", inclusive = true, message = "cbamFactor must be between 0 and 1")
    private BigDecimal cbamFactor;

    /** Carbon price already paid in the third country, in EUR per tCO2e. Example: 0. */
    @NotNull(message = "thirdCountryCarbonPriceEurPerTco2e is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "thirdCountryCarbonPriceEurPerTco2e must be >= 0")
    private BigDecimal thirdCountryCarbonPriceEurPerTco2e;

    /** CBAM certificate price in EUR per tCO2e. Example: 76. */
    @NotNull(message = "cbamCertificatePriceEurPerTco2e is required")
    @DecimalMin(value = "0.0001", message = "cbamCertificatePriceEurPerTco2e must be > 0")
    private BigDecimal cbamCertificatePriceEurPerTco2e;

    /** Imported quantity in tons. Example: 100. */
    @NotNull(message = "importedQuantityTons is required")
    @DecimalMin(value = "0.0001", message = "importedQuantityTons must be > 0")
    private BigDecimal importedQuantityTons;

    public BigDecimal getActualSpecificEmissionsTco2ePerTon() {
        return actualSpecificEmissionsTco2ePerTon;
    }

    public void setActualSpecificEmissionsTco2ePerTon(BigDecimal actualSpecificEmissionsTco2ePerTon) {
        this.actualSpecificEmissionsTco2ePerTon = actualSpecificEmissionsTco2ePerTon;
    }

    public BigDecimal getCbamBenchmarkTco2ePerTon() {
        return cbamBenchmarkTco2ePerTon;
    }

    public void setCbamBenchmarkTco2ePerTon(BigDecimal cbamBenchmarkTco2ePerTon) {
        this.cbamBenchmarkTco2ePerTon = cbamBenchmarkTco2ePerTon;
    }

    public BigDecimal getCbamFactor() {
        return cbamFactor;
    }

    public void setCbamFactor(BigDecimal cbamFactor) {
        this.cbamFactor = cbamFactor;
    }

    public BigDecimal getThirdCountryCarbonPriceEurPerTco2e() {
        return thirdCountryCarbonPriceEurPerTco2e;
    }

    public void setThirdCountryCarbonPriceEurPerTco2e(BigDecimal thirdCountryCarbonPriceEurPerTco2e) {
        this.thirdCountryCarbonPriceEurPerTco2e = thirdCountryCarbonPriceEurPerTco2e;
    }

    public BigDecimal getCbamCertificatePriceEurPerTco2e() {
        return cbamCertificatePriceEurPerTco2e;
    }

    public void setCbamCertificatePriceEurPerTco2e(BigDecimal cbamCertificatePriceEurPerTco2e) {
        this.cbamCertificatePriceEurPerTco2e = cbamCertificatePriceEurPerTco2e;
    }

    public BigDecimal getImportedQuantityTons() {
        return importedQuantityTons;
    }

    public void setImportedQuantityTons(BigDecimal importedQuantityTons) {
        this.importedQuantityTons = importedQuantityTons;
    }
}
