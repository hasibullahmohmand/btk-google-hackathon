package com.carbonai.cbam.dto;

import com.carbonai.cbam.model.ActivityInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request body for /api/cbam/actual-emissions.
 *
 * What this DTO means:
 * Captures factory activity data used to calculate actual direct and indirect emissions.
 *
 * Beginner-friendly explanation:
 * This request is used when the exporter has real production and activity data
 * and wants an actual-data-based emissions calculation instead of a fallback.
 *
 * Example request:
 * {
 *   "cnCode": "72142000",
 *   "country": "Turkey",
 *   "year": 2026,
 *   "productionVolumeTons": 100,
 *   "exportVolumeTons": 40,
 *   "activities": [
 *     {
 *       "activityType": "Natural gas",
 *       "amount": 50,
 *       "unit": "t"
 *     },
 *     {
 *       "activityType": "Gas/Diesel oil",
 *       "amount": 5,
 *       "unit": "t"
 *     }
 *   ]
 * }
 */
public class ActualEmissionsRequest {

    /** CN code of the product whose embedded emissions are being calculated. */
    @NotBlank(message = "cnCode is required")
    private String cnCode;

    /** Country of the installation or exporter. */
    @NotBlank(message = "country is required")
    private String country;

    /** Reporting year used for year-aware factor tables. */
    @NotNull(message = "year is required")
    private Integer year;

    /** Total production volume in tons. Example: 100. */
    @NotNull(message = "productionVolumeTons is required")
    @DecimalMin(value = "0.0001", message = "productionVolumeTons must be greater than 0")
    private BigDecimal productionVolumeTons;

    /** Exported share of production in tons. Example: 40. */
    @NotNull(message = "exportVolumeTons is required")
    @DecimalMin(value = "0.0001", message = "exportVolumeTons must be greater than 0")
    private BigDecimal exportVolumeTons;

    /**
     * Optional backward-compatible request flag.
     * The current calculation still includes indirect emissions when such rows
     * are provided, but the field is no longer mandatory.
     */
    private Boolean includeIndirectEmissions;

    /** List of activity rows such as natural gas, diesel, and electricity usage. */
    @Valid
    @NotEmpty(message = "activities cannot be empty")
    private List<ActivityInput> activities;

    public String getCnCode() {
        return cnCode;
    }

    public void setCnCode(String cnCode) {
        this.cnCode = cnCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    public Boolean getIncludeIndirectEmissions() {
        return includeIndirectEmissions;
    }

    public void setIncludeIndirectEmissions(Boolean includeIndirectEmissions) {
        this.includeIndirectEmissions = includeIndirectEmissions;
    }

    public List<ActivityInput> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityInput> activities) {
        this.activities = activities;
    }
}
