package com.carbonai.cbam.dto;

import com.carbonai.cbam.model.ActivityEmissionBreakdown;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response body for /api/cbam/actual-emissions.
 *
 * Beginner-friendly explanation:
 * This response explains how the activity inputs were turned into direct,
 * indirect, total, per-ton, and export-only emissions values.
 *
 * Example response:
 * {
 *   "product": "steel",
 *   "productionVolumeTons": 100,
 *   "exportVolumeTons": 40,
 *   "directEmissionsTco2e": 13.2160,
 *   "indirectEmissionsTco2e": 10.5000,
 *   "totalFacilityEmissionsTco2e": 23.7160,
 *   "specificEmissionsTco2ePerTon": 0.2372,
 *   "exportedEmbeddedEmissionsTco2e": 9.4864,
 *   "includeIndirectEmissions": true,
 *   "calculationMode": "ACTUAL_DATA",
 *   "activityBreakdown": [],
 *   "warnings": []
 * }
 */
public class ActualEmissionsResponse {

    /** Product label echoed from the request. */
    private String product;
    /** Total production volume used as the denominator for specific emissions. */
    private BigDecimal productionVolumeTons;
    /** Export volume used to calculate exported embedded emissions. */
    private BigDecimal exportVolumeTons;
    /** Sum of non-electricity emissions in tCO2e. */
    private BigDecimal directEmissionsTco2e;
    /** Sum of electricity-related emissions in tCO2e. */
    private BigDecimal indirectEmissionsTco2e;
    /** Total facility emissions included in the calculation, in tCO2e. */
    private BigDecimal totalFacilityEmissionsTco2e;
    /** Emissions intensity per ton of product, in tCO2e per ton. */
    private BigDecimal specificEmissionsTco2ePerTon;
    /** Export-only share of embedded emissions, in tCO2e. */
    private BigDecimal exportedEmbeddedEmissionsTco2e;
    /** Indicates whether indirect emissions were included in the total. */
    private Boolean includeIndirectEmissions;
    /** Indicates that actual activity data was used. */
    private String calculationMode;
    /** Row-by-row breakdown of activity calculations. */
    private List<ActivityEmissionBreakdown> activityBreakdown;
    /** Non-blocking input warnings. */
    private List<String> warnings;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public BigDecimal getProductionVolumeTons() {
        return productionVolumeTons;
    }

    public void setProductionVolumeTons(BigDecimal productionVolumeTons) {
        this.productionVolumeTons = productionVolumeTons;
    }

    public BigDecimal getExportVolumeTons() {
        return exportVolumeTons;
    }

    public void setExportVolumeTons(BigDecimal exportVolumeTons) {
        this.exportVolumeTons = exportVolumeTons;
    }

    public BigDecimal getDirectEmissionsTco2e() {
        return directEmissionsTco2e;
    }

    public void setDirectEmissionsTco2e(BigDecimal directEmissionsTco2e) {
        this.directEmissionsTco2e = directEmissionsTco2e;
    }

    public BigDecimal getIndirectEmissionsTco2e() {
        return indirectEmissionsTco2e;
    }

    public void setIndirectEmissionsTco2e(BigDecimal indirectEmissionsTco2e) {
        this.indirectEmissionsTco2e = indirectEmissionsTco2e;
    }

    public BigDecimal getTotalFacilityEmissionsTco2e() {
        return totalFacilityEmissionsTco2e;
    }

    public void setTotalFacilityEmissionsTco2e(BigDecimal totalFacilityEmissionsTco2e) {
        this.totalFacilityEmissionsTco2e = totalFacilityEmissionsTco2e;
    }

    public BigDecimal getSpecificEmissionsTco2ePerTon() {
        return specificEmissionsTco2ePerTon;
    }

    public void setSpecificEmissionsTco2ePerTon(BigDecimal specificEmissionsTco2ePerTon) {
        this.specificEmissionsTco2ePerTon = specificEmissionsTco2ePerTon;
    }

    public BigDecimal getExportedEmbeddedEmissionsTco2e() {
        return exportedEmbeddedEmissionsTco2e;
    }

    public void setExportedEmbeddedEmissionsTco2e(BigDecimal exportedEmbeddedEmissionsTco2e) {
        this.exportedEmbeddedEmissionsTco2e = exportedEmbeddedEmissionsTco2e;
    }

    public Boolean getIncludeIndirectEmissions() {
        return includeIndirectEmissions;
    }

    public void setIncludeIndirectEmissions(Boolean includeIndirectEmissions) {
        this.includeIndirectEmissions = includeIndirectEmissions;
    }

    public String getCalculationMode() {
        return calculationMode;
    }

    public void setCalculationMode(String calculationMode) {
        this.calculationMode = calculationMode;
    }

    public List<ActivityEmissionBreakdown> getActivityBreakdown() {
        return activityBreakdown;
    }

    public void setActivityBreakdown(List<ActivityEmissionBreakdown> activityBreakdown) {
        this.activityBreakdown = activityBreakdown;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
}
