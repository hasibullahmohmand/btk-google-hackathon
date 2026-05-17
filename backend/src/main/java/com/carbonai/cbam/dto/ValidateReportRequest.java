package com.carbonai.cbam.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request body for /api/cbam/validate-report.
 *
 * Beginner-friendly explanation:
 * This request represents one report line that should be checked for required
 * fields and internal consistency before export or submission.
 *
 * Example request:
 * {
 *   "goodsItemNumber": "1",
 *   "sequenceNumber": "1",
 *   "cnCode": "25233000",
 *   "country": "Turkey",
 *   "period": "2026",
 *   "directEmissionsTco2e": 1.82,
 *   "indirectEmissionsTco2e": 0.14,
 *   "totalEmissionsTco2e": 1.96,
 *   "netMassTons": 100
 * }
 */
public class ValidateReportRequest {

    /** Goods item number. Example: "1". */
    @NotBlank(message = "goodsItemNumber is required")
    private String goodsItemNumber;

    /** Sequence number. Example: "1". */
    @NotBlank(message = "sequenceNumber is required")
    private String sequenceNumber;

    /** Customs CN code. Example: "25233000". */
    @NotBlank(message = "cnCode is required")
    private String cnCode;

    /** Country name. Example: "Turkey". */
    @NotBlank(message = "country is required")
    private String country;

    /** Reporting period. Example: "2026". */
    @NotBlank(message = "period is required")
    private String period;

    /** Direct emissions amount in tCO2e. Example: 1.82. */
    @NotNull(message = "directEmissionsTco2e is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "directEmissionsTco2e must be >= 0")
    private BigDecimal directEmissionsTco2e;

    /** Indirect emissions amount in tCO2e. Example: 0.14. */
    @NotNull(message = "indirectEmissionsTco2e is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "indirectEmissionsTco2e must be >= 0")
    private BigDecimal indirectEmissionsTco2e;

    /** Reported total emissions amount in tCO2e. Example: 1.96. */
    @NotNull(message = "totalEmissionsTco2e is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "totalEmissionsTco2e must be >= 0")
    private BigDecimal totalEmissionsTco2e;

    /** Net mass in tons. Example: 100. */
    @NotNull(message = "netMassTons is required")
    @DecimalMin(value = "0.0001", message = "netMassTons must be greater than zero")
    private BigDecimal netMassTons;

    public String getGoodsItemNumber() {
        return goodsItemNumber;
    }

    public void setGoodsItemNumber(String goodsItemNumber) {
        this.goodsItemNumber = goodsItemNumber;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
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

    public BigDecimal getTotalEmissionsTco2e() {
        return totalEmissionsTco2e;
    }

    public void setTotalEmissionsTco2e(BigDecimal totalEmissionsTco2e) {
        this.totalEmissionsTco2e = totalEmissionsTco2e;
    }

    public BigDecimal getNetMassTons() {
        return netMassTons;
    }

    public void setNetMassTons(BigDecimal netMassTons) {
        this.netMassTons = netMassTons;
    }
}
