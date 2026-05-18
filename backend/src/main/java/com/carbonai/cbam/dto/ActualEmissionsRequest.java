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
 *   "product": "steel",
 *   "productionVolumeTons": 100,
 *   "exportVolumeTons": 40,
 *   "includeIndirectEmissions": true,
 *   "activities": [
 *     {
 *       "activityType": "NATURAL_GAS",
 *       "amount": 5000,
 *       "unit": "m3"
 *     },
 *     {
 *       "activityType": "DIESEL",
 *       "amount": 1200,
 *       "unit": "liter"
 *     },
 *     {
 *       "activityType": "ELECTRICITY",
 *       "amount": 25000,
 *       "unit": "kWh"
 *     }
 *   ]
 * }
 */
public class ActualEmissionsRequest {

    /** Product label used for display. Example: "steel". */
    @NotBlank(message = "product is required")
    private String product;

    /** Total production volume in tons. Example: 100. */
    @NotNull(message = "productionVolumeTons is required")
    @DecimalMin(value = "0.0001", message = "productionVolumeTons must be greater than 0")
    private BigDecimal productionVolumeTons;

    /** Exported share of production in tons. Example: 40. */
    @NotNull(message = "exportVolumeTons is required")
    @DecimalMin(value = "0.0001", message = "exportVolumeTons must be greater than 0")
    private BigDecimal exportVolumeTons;

    /**
     * Backward-compatible request flag.
     * The current CBAM-aligned calculation always includes indirect emissions in embedded emissions totals.
     */
    @NotNull(message = "includeIndirectEmissions is required")
    private Boolean includeIndirectEmissions;

    /** List of activity rows such as natural gas, diesel, and electricity usage. */
    @Valid
    @NotEmpty(message = "activities cannot be empty")
    private List<ActivityInput> activities;

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
