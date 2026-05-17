package com.carbonai.cbam.dto;

import java.math.BigDecimal;

/**
 * Response body for /api/cbam/advanced-certificates.
 *
 * Beginner-friendly explanation:
 * This response shows the deduction values, the final estimated certificate
 * quantity, and the related EUR exposure.
 *
 * Example response:
 * {
 *   "actualSpecificEmissionsTco2ePerTon": 2.1450,
 *   "freeAllowanceDeductionTco2ePerTon": 1.4625,
 *   "thirdCountryCarbonPriceDeductionTco2ePerTon": 0.0000,
 *   "importedQuantityTons": 100,
 *   "certificatesToSurrender": 68.2500,
 *   "cbamCertificatePriceEurPerTco2e": 76,
 *   "estimatedCostEur": 5187.00,
 *   "formula": "certificates = max(0, (A - B - C) × D)"
 * }
 */
public class AdvancedCertificatesResponse {

    /** A in the formula: actual specific emissions. */
    private BigDecimal actualSpecificEmissionsTco2ePerTon;
    /** B in the formula: free allowance deduction. */
    private BigDecimal freeAllowanceDeductionTco2ePerTon;
    /** C in the formula: third-country carbon price deduction. */
    private BigDecimal thirdCountryCarbonPriceDeductionTco2ePerTon;
    /** D in the formula: imported quantity in tons. */
    private BigDecimal importedQuantityTons;
    /** Final estimated number of certificates to surrender. */
    private BigDecimal certificatesToSurrender;
    /** Certificate price in EUR per tCO2e. */
    private BigDecimal cbamCertificatePriceEurPerTco2e;
    /** Estimated EUR exposure after calculating certificates. */
    private BigDecimal estimatedCostEur;
    /** Human-readable formula string. */
    private String formula;

    public BigDecimal getActualSpecificEmissionsTco2ePerTon() {
        return actualSpecificEmissionsTco2ePerTon;
    }

    public void setActualSpecificEmissionsTco2ePerTon(BigDecimal actualSpecificEmissionsTco2ePerTon) {
        this.actualSpecificEmissionsTco2ePerTon = actualSpecificEmissionsTco2ePerTon;
    }

    public BigDecimal getFreeAllowanceDeductionTco2ePerTon() {
        return freeAllowanceDeductionTco2ePerTon;
    }

    public void setFreeAllowanceDeductionTco2ePerTon(BigDecimal freeAllowanceDeductionTco2ePerTon) {
        this.freeAllowanceDeductionTco2ePerTon = freeAllowanceDeductionTco2ePerTon;
    }

    public BigDecimal getThirdCountryCarbonPriceDeductionTco2ePerTon() {
        return thirdCountryCarbonPriceDeductionTco2ePerTon;
    }

    public void setThirdCountryCarbonPriceDeductionTco2ePerTon(BigDecimal thirdCountryCarbonPriceDeductionTco2ePerTon) {
        this.thirdCountryCarbonPriceDeductionTco2ePerTon = thirdCountryCarbonPriceDeductionTco2ePerTon;
    }

    public BigDecimal getImportedQuantityTons() {
        return importedQuantityTons;
    }

    public void setImportedQuantityTons(BigDecimal importedQuantityTons) {
        this.importedQuantityTons = importedQuantityTons;
    }

    public BigDecimal getCertificatesToSurrender() {
        return certificatesToSurrender;
    }

    public void setCertificatesToSurrender(BigDecimal certificatesToSurrender) {
        this.certificatesToSurrender = certificatesToSurrender;
    }

    public BigDecimal getCbamCertificatePriceEurPerTco2e() {
        return cbamCertificatePriceEurPerTco2e;
    }

    public void setCbamCertificatePriceEurPerTco2e(BigDecimal cbamCertificatePriceEurPerTco2e) {
        this.cbamCertificatePriceEurPerTco2e = cbamCertificatePriceEurPerTco2e;
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
