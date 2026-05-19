package com.carbonai.cbam.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request body for /api/cbam/default-emissions.
 *
 * What this DTO means:
 * Requests a default-value based emissions calculation using seeded country and CN code values.
 *
 * Beginner-friendly explanation:
 * This request is used when the exporter knows the product and quantity but
 * does not know the actual factory emissions data.
 *
 * Example request:
 * {
 *   "country": "Turkey",
 *   "cnCode": "25233000",
 *   "year": 2026,
 *   "exportVolumeTons": 100
 * }
 */
public class DefaultEmissionsRequest {

    /** Exporting country. Example: "Turkey". */
    @NotBlank(message = "country is required")
    private String country;

    /** Customs CN code that identifies the product. Example: "25233000". */
    @NotBlank(message = "cnCode is required")
    private String cnCode;

    /** CBAM year used to choose the correct CSV-backed default value. Example: 2026. */
    @NotNull(message = "year is required")
    @Min(value = 2023, message = "year must be >= 2023")
    private Integer year;

    /** Export quantity in tons. Example: 100. */
    @NotNull(message = "exportVolumeTons is required")
    @DecimalMin(value = "0.0001", message = "exportVolumeTons must be > 0")
    private BigDecimal exportVolumeTons;

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
}
