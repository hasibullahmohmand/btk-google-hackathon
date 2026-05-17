package com.carbonai.cbam.dto;

import java.math.BigDecimal;

/**
 * Response body for /api/cbam/default-emissions.
 *
 * Beginner-friendly explanation:
 * This response shows the fallback emissions intensity that was selected and
 * the total embedded emissions estimated for the shipment.
 *
 * Example response:
 * {
 *   "country": "Turkey",
 *   "cnCode": "25233000",
 *   "productDescription": "Aluminous cement",
 *   "year": 2026,
 *   "exportVolumeTons": 100,
 *   "selectedDefaultValueTco2ePerTon": 2.145,
 *   "embeddedEmissionsTco2e": 214.5,
 *   "calculationMode": "DEFAULT_VALUE",
 *   "formula": "embeddedEmissions = exportVolumeTons × defaultValueTco2ePerTon"
 * }
 */
public class DefaultEmissionsResponse {

    /** Country returned from the matched seeded record. */
    private String country;
    /** CN code returned from the matched seeded record. */
    private String cnCode;
    /** Human-readable product description. Example: "Aluminous cement". */
    private String productDescription;
    /** Year used in the default value lookup. */
    private Integer year;
    /** Export quantity in tons. */
    private BigDecimal exportVolumeTons;
    /** Selected default emissions intensity in tCO2e per ton. */
    private BigDecimal selectedDefaultValueTco2ePerTon;
    /** Final embedded emissions for the shipment, in tCO2e. */
    private BigDecimal embeddedEmissionsTco2e;
    /** Indicates that this result came from default values. */
    private String calculationMode;
    /** Human-readable formula string used by the calculator. */
    private String formula;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCnCode() {
        return cnCode;
    }

    public void setCnCode(String cnCode) {
        this.cnCode = cnCode;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getExportVolumeTons() {
        return exportVolumeTons;
    }

    public void setExportVolumeTons(BigDecimal exportVolumeTons) {
        this.exportVolumeTons = exportVolumeTons;
    }

    public BigDecimal getSelectedDefaultValueTco2ePerTon() {
        return selectedDefaultValueTco2ePerTon;
    }

    public void setSelectedDefaultValueTco2ePerTon(BigDecimal selectedDefaultValueTco2ePerTon) {
        this.selectedDefaultValueTco2ePerTon = selectedDefaultValueTco2ePerTon;
    }

    public BigDecimal getEmbeddedEmissionsTco2e() {
        return embeddedEmissionsTco2e;
    }

    public void setEmbeddedEmissionsTco2e(BigDecimal embeddedEmissionsTco2e) {
        this.embeddedEmissionsTco2e = embeddedEmissionsTco2e;
    }

    public String getCalculationMode() {
        return calculationMode;
    }

    public void setCalculationMode(String calculationMode) {
        this.calculationMode = calculationMode;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
