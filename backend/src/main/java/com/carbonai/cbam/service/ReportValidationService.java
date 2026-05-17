package com.carbonai.cbam.service;

import com.carbonai.cbam.dto.ValidateReportRequest;
import com.carbonai.cbam.dto.ValidateReportResponse;
import com.carbonai.cbam.model.ValidationError;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for CBAM-style report validation.
 *
 * Beginner-friendly explanation:
 * This service does not calculate emissions or submit anything to the EU. It
 * simply checks whether a report payload is complete and internally consistent.
 */
@Service
public class ReportValidationService {

    private static final BigDecimal TOLERANCE = new BigDecimal("0.0001");

    /**
     * Validates a generated CBAM-ready report payload.
     *
     * Business meaning:
     * Checks structure and consistency rules for a report before export or
     * submission. It does not submit anything to an official CBAM registry.
     *
     * Parameters:
     * request.goodsItemNumber = goods item id, example "1"
     * request.sequenceNumber = sequence number, example "1"
     * request.cnCode = customs CN code, example "25233000"
     * request.country = country name, example "Turkey"
     * request.period = reporting period, example "2026"
     * request.directEmissionsTco2e = direct emissions, example 1.82
     * request.indirectEmissionsTco2e = indirect emissions, example 0.14
     * request.totalEmissionsTco2e = total emissions, example 1.96
     * request.netMassTons = product quantity, example 100
     *
     * Example output values:
     * valid = true, errors = []
     * or
     * valid = false with R0010 mismatch error
     *
     * Formula used:
     * expectedTotal = directEmissionsTco2e + indirectEmissionsTco2e
     * R0010 passes when absolute(expectedTotal - totalEmissionsTco2e) <= 0.0001
     *
     * Step-by-step example calculation:
     * 1. directEmissionsTco2e = 1.82
     * 2. indirectEmissionsTco2e = 0.14
     * 3. expectedTotal = 1.82 + 0.14 = 1.96
     * 4. if reported total = 1.95, the difference is 0.01
     * 5. 0.01 is greater than tolerance 0.0001, so rule R0010 fails
     *
     * Output example:
     * {
     *   "valid": false,
     *   "errors": [
     *     {
     *       "code": "R0010",
     *       "expectedValue": 1.9600,
     *       "actualValue": 1.9500
     *     }
     *   ]
     * }
     */
    public ValidateReportResponse validateReport(ValidateReportRequest request) {
        List<ValidationError> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // Each rule is written separately so the validation response remains
        // easy to read for beginners and frontend developers.
        if (isBlank(request.getGoodsItemNumber())) {
            errors.add(new ValidationError("R0001", "Goods item number is required.", null, null));
        }
        if (isBlank(request.getGoodsItemNumber())) {
            errors.add(new ValidationError("R0002", "Goods item number must be unique.", null, null));
        }
        if (isBlank(request.getSequenceNumber())) {
            errors.add(new ValidationError("R0003", "Sequence number is required.", null, null));
        }
        if (isBlank(request.getCnCode())) {
            errors.add(new ValidationError("R0004", "CN code is required.", null, null));
        }
        if (isBlank(request.getCountry())) {
            errors.add(new ValidationError("R0005", "Country is required.", null, null));
        }
        if (isBlank(request.getPeriod())) {
            errors.add(new ValidationError("R0006", "Period is required.", null, null));
        }
        if (request.getNetMassTons() != null && request.getNetMassTons().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(new ValidationError("R0007", "Net mass must be greater than zero.", BigDecimal.ZERO, request.getNetMassTons()));
        }

        if (request.getDirectEmissionsTco2e() != null
                && request.getIndirectEmissionsTco2e() != null
                && request.getTotalEmissionsTco2e() != null) {
            // Recalculate the expected total instead of trusting the provided total.
            BigDecimal expectedTotal = request.getDirectEmissionsTco2e().add(request.getIndirectEmissionsTco2e());
            BigDecimal difference = expectedTotal.subtract(request.getTotalEmissionsTco2e()).abs();
            if (difference.compareTo(TOLERANCE) > 0) {
                errors.add(new ValidationError(
                        "R0010",
                        "Total emissions must equal direct emissions plus indirect emissions.",
                        CalculationSupport.roundEmissions(expectedTotal),
                        CalculationSupport.roundEmissions(request.getTotalEmissionsTco2e())
                ));
            }
        }

        ValidateReportResponse response = new ValidateReportResponse();
        response.setValid(errors.isEmpty());
        response.setErrors(errors);
        response.setWarnings(warnings);
        return response;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
