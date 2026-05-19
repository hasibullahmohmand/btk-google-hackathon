package com.carbonai.cbam.service;

import com.carbonai.cbam.dto.CompareDefaultVsActualRequest;
import com.carbonai.cbam.dto.CompareDefaultVsActualResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for cost-related comparison calculations.
 */
@Service
public class CbamCostService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    /**
     * Compares default-value cost with actual-data cost and computes savings.
     *
     * Business meaning:
     * Calculates two costs with the same export volume and carbon price, then
     * shows the EUR difference and percentage savings from using actual data.
     *
     * Parameters:
     * request.defaultSpecificEmbeddedEmissionsTco2ePerTon = default emission intensity, example 2.145
     * request.actualSpecificEmbeddedEmissionsTco2ePerTon = actual emission intensity, example 1.6
     * request.exportVolumeTons = quantity, example 100
     * request.euEtsWeeklyAveragePriceEurPerTco2e = ETS-linked price, example 76
     *
     * Example output values:
     * defaultCostEur = 16302.00
     * actualCostEur = 12160.00
     * potentialSavingsEur = 4142.00
     * savingsPercent = 25.41
     *
     * Formula used:
     * defaultCost = defaultSpecificEmbeddedEmissions x exportVolume x euEtsWeeklyAveragePrice
     * actualCost = actualSpecificEmbeddedEmissions x exportVolume x euEtsWeeklyAveragePrice
     * potentialSavings = defaultCost - actualCost
     * savingsPercent = (potentialSavings / defaultCost) x 100
     *
     * Step-by-step example calculation:
     * 1. Default cost:
     *    2.145 x 100 x 76 = 16302
     * 2. Actual cost:
     *    1.6 x 100 x 76 = 12160
     * 3. Potential savings:
     *    16302 - 12160 = 4142
     * 4. Savings percent:
     *    4142 / 16302 x 100 = 25.41%
     *
     * Output example:
     * {
     *   "defaultCostEur": 16302.00,
     *   "actualCostEur": 12160.00,
     *   "potentialSavingsEur": 4142.00,
     *   "savingsPercent": 25.41
     * }
     */
    public CompareDefaultVsActualResponse compareDefaultVsActual(CompareDefaultVsActualRequest request) {
        // Source traceability:
        // - pdfs/outputs/raw_markdown/CBAM Frequently Asked Questions_November 2023.md
        //   states that actual values can lower the CBAM payment compared with default values.
        // This endpoint turns that policy idea into a project-specific comparison calculation.
        BigDecimal defaultCost = request.getDefaultSpecificEmbeddedEmissionsTco2ePerTon()
                .multiply(request.getExportVolumeTons())
                .multiply(request.getEuEtsWeeklyAveragePriceEurPerTco2e());
        BigDecimal actualCost = request.getActualSpecificEmbeddedEmissionsTco2ePerTon()
                .multiply(request.getExportVolumeTons())
                .multiply(request.getEuEtsWeeklyAveragePriceEurPerTco2e());
        BigDecimal savings = defaultCost.subtract(actualCost);
        BigDecimal savingsPercent = defaultCost.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : savings.divide(defaultCost, 8, CalculationSupport.ROUNDING_MODE).multiply(ONE_HUNDRED);

        CompareDefaultVsActualResponse response = new CompareDefaultVsActualResponse();
        response.setDefaultCostEur(CalculationSupport.roundMoney(defaultCost));
        response.setActualCostEur(CalculationSupport.roundMoney(actualCost));
        response.setPotentialSavingsEur(CalculationSupport.roundMoney(savings));
        response.setSavingsPercent(CalculationSupport.roundPercent(savingsPercent));
        response.setMessage("Using actual embedded emissions instead of default values may reduce ETS-linked CBAM certificate cost exposure.");
        return response;
    }
}
