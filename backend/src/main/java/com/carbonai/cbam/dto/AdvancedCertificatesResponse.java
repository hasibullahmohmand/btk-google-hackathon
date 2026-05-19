package com.carbonai.cbam.dto;

import java.math.BigDecimal;

/**
 * Response body for /api/cbam/advanced-certificates.
 *
 * CBAM source-of-truth alignment:
 * The response exposes the steps described in the raw markdown:
 * - total embedded emissions for the import;
 * - free-allocation-adjusted certificate obligation;
 * - reduction for the effective carbon price paid abroad;
 * - final certificates to surrender and the ETS-linked cost.
 *
 * Example response:
 * {
 *   "actualSpecificEmbeddedEmissionsTco2ePerTon": 2.1450,
 *   "specificEmbeddedFreeAllocationTco2ePerTon": 1.4625,
 *   "importedQuantityTons": 100,
 *   "totalEmbeddedEmissionsTco2e": 214.5000,
 *   "certificateSurrenderObligationBeforeCarbonPriceAdjustment": 68.2500,
 *   "carbonPriceReductionInCertificates": 0.0000,
 *   "certificatesToSurrender": 68.2500,
 *   "effectiveCarbonPricePaidInCountryOfOriginEurPerTco2e": 0,
 *   "euEtsWeeklyAveragePriceEurPerTco2e": 76,
 *   "estimatedCostEur": 5187.00,
 *   "formula": "certificatesBeforeCarbonPriceAdjustment = max(0, (specificEmbeddedEmissions - specificEmbeddedFreeAllocation) x importedQuantity)"
 * }
 */
public class AdvancedCertificatesResponse {

    /** Specific embedded emissions of the imported good, in tCO2e per tonne. */
    private BigDecimal actualSpecificEmbeddedEmissionsTco2ePerTon;
    /** Specific embedded free allocation adjustment, in tCO2e per tonne. */
    private BigDecimal specificEmbeddedFreeAllocationTco2ePerTon;
    /** Imported quantity in tonnes. */
    private BigDecimal importedQuantityTons;
    /** Total embedded emissions for the import before any adjustments. */
    private BigDecimal totalEmbeddedEmissionsTco2e;
    /** Certificate obligation after free allocation adjustment but before carbon price reduction. */
    private BigDecimal certificateSurrenderObligationBeforeCarbonPriceAdjustment;
    /** Reduction in certificates due to the effective carbon price paid in the country of origin. */
    private BigDecimal carbonPriceReductionInCertificates;
    /** Final estimated number of certificates to surrender. */
    private BigDecimal certificatesToSurrender;
    /** Carbon price effectively paid in the country of origin, in EUR per tCO2e. */
    private BigDecimal effectiveCarbonPricePaidInCountryOfOriginEurPerTco2e;
    /** ETS-linked certificate price in EUR per tCO2e. */
    private BigDecimal euEtsWeeklyAveragePriceEurPerTco2e;
    /** Estimated EUR exposure after calculating certificates. */
    private BigDecimal estimatedCostEur;
    /** Human-readable formula string. */
    private String formula;

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

    public BigDecimal getImportedQuantityTons() {
        return importedQuantityTons;
    }

    public void setImportedQuantityTons(BigDecimal importedQuantityTons) {
        this.importedQuantityTons = importedQuantityTons;
    }

    public BigDecimal getTotalEmbeddedEmissionsTco2e() {
        return totalEmbeddedEmissionsTco2e;
    }

    public void setTotalEmbeddedEmissionsTco2e(BigDecimal totalEmbeddedEmissionsTco2e) {
        this.totalEmbeddedEmissionsTco2e = totalEmbeddedEmissionsTco2e;
    }

    public BigDecimal getCertificateSurrenderObligationBeforeCarbonPriceAdjustment() {
        return certificateSurrenderObligationBeforeCarbonPriceAdjustment;
    }

    public void setCertificateSurrenderObligationBeforeCarbonPriceAdjustment(BigDecimal certificateSurrenderObligationBeforeCarbonPriceAdjustment) {
        this.certificateSurrenderObligationBeforeCarbonPriceAdjustment = certificateSurrenderObligationBeforeCarbonPriceAdjustment;
    }

    public BigDecimal getCarbonPriceReductionInCertificates() {
        return carbonPriceReductionInCertificates;
    }

    public void setCarbonPriceReductionInCertificates(BigDecimal carbonPriceReductionInCertificates) {
        this.carbonPriceReductionInCertificates = carbonPriceReductionInCertificates;
    }

    public BigDecimal getCertificatesToSurrender() {
        return certificatesToSurrender;
    }

    public void setCertificatesToSurrender(BigDecimal certificatesToSurrender) {
        this.certificatesToSurrender = certificatesToSurrender;
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
