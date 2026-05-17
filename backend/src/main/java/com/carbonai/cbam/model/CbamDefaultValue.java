package com.carbonai.cbam.model;

import java.math.BigDecimal;

/**
 * Seeded CBAM default emission values for a country + CN code combination.
 *
 * Beginner-friendly explanation:
 * This model stores fallback emissions intensities that can be used when the
 * company does not have actual plant data.
 */
public class CbamDefaultValue {

    /** Exporting country. Example: Turkey. */
    private String country;
    /** Customs CN code. Example: 25233000. */
    private String cnCode;
    /** Human-readable product name. */
    private String productDescription;
    /** Product sector. Example: Cement. */
    private String sector;
    /** Direct default emissions intensity in tCO2e/t. */
    private BigDecimal directDefaultTco2ePerTon;
    /** Indirect default emissions intensity in tCO2e/t. */
    private BigDecimal indirectDefaultTco2ePerTon;
    /** Total default emissions intensity in tCO2e/t. */
    private BigDecimal totalDefaultTco2ePerTon;
    /** Year-specific default value for 2026 after markup. */
    private BigDecimal default2026WithMarkup;
    /** Year-specific default value for 2027 after markup. */
    private BigDecimal default2027WithMarkup;
    /** Year-specific default value for 2028 and later after markup. */
    private BigDecimal default2028OnwardsWithMarkup;

    public CbamDefaultValue() {
    }

    public CbamDefaultValue(String country,
                            String cnCode,
                            String productDescription,
                            String sector,
                            BigDecimal directDefaultTco2ePerTon,
                            BigDecimal indirectDefaultTco2ePerTon,
                            BigDecimal totalDefaultTco2ePerTon,
                            BigDecimal default2026WithMarkup,
                            BigDecimal default2027WithMarkup,
                            BigDecimal default2028OnwardsWithMarkup) {
        this.country = country;
        this.cnCode = cnCode;
        this.productDescription = productDescription;
        this.sector = sector;
        this.directDefaultTco2ePerTon = directDefaultTco2ePerTon;
        this.indirectDefaultTco2ePerTon = indirectDefaultTco2ePerTon;
        this.totalDefaultTco2ePerTon = totalDefaultTco2ePerTon;
        this.default2026WithMarkup = default2026WithMarkup;
        this.default2027WithMarkup = default2027WithMarkup;
        this.default2028OnwardsWithMarkup = default2028OnwardsWithMarkup;
    }

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

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public BigDecimal getDirectDefaultTco2ePerTon() {
        return directDefaultTco2ePerTon;
    }

    public void setDirectDefaultTco2ePerTon(BigDecimal directDefaultTco2ePerTon) {
        this.directDefaultTco2ePerTon = directDefaultTco2ePerTon;
    }

    public BigDecimal getIndirectDefaultTco2ePerTon() {
        return indirectDefaultTco2ePerTon;
    }

    public void setIndirectDefaultTco2ePerTon(BigDecimal indirectDefaultTco2ePerTon) {
        this.indirectDefaultTco2ePerTon = indirectDefaultTco2ePerTon;
    }

    public BigDecimal getTotalDefaultTco2ePerTon() {
        return totalDefaultTco2ePerTon;
    }

    public void setTotalDefaultTco2ePerTon(BigDecimal totalDefaultTco2ePerTon) {
        this.totalDefaultTco2ePerTon = totalDefaultTco2ePerTon;
    }

    public BigDecimal getDefault2026WithMarkup() {
        return default2026WithMarkup;
    }

    public void setDefault2026WithMarkup(BigDecimal default2026WithMarkup) {
        this.default2026WithMarkup = default2026WithMarkup;
    }

    public BigDecimal getDefault2027WithMarkup() {
        return default2027WithMarkup;
    }

    public void setDefault2027WithMarkup(BigDecimal default2027WithMarkup) {
        this.default2027WithMarkup = default2027WithMarkup;
    }

    public BigDecimal getDefault2028OnwardsWithMarkup() {
        return default2028OnwardsWithMarkup;
    }

    public void setDefault2028OnwardsWithMarkup(BigDecimal default2028OnwardsWithMarkup) {
        this.default2028OnwardsWithMarkup = default2028OnwardsWithMarkup;
    }
}
