package com.carbonai.cbam.service;

import com.carbonai.cbam.dto.CompareDefaultVsActualRequest;
import com.carbonai.cbam.dto.CompareDefaultVsActualResponse;
import com.carbonai.cbam.dto.SimpleCostRequest;
import com.carbonai.cbam.dto.SimpleCostResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for cost-related calculations.
 *
 * Beginner-friendly explanation:
 * These methods translate emissions into money. They do not decide any legal
 * obligation by themselves, but they help the business understand likely carbon
 * cost exposure in EUR.
 */
@Service
public class CbamCostService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    /**
     * Calculates simple estimated CBAM financial exposure.
     *
     * Business meaning:
     * Multiplies embedded emissions by the certificate price.
     *
     * Parameters:
     * request.embeddedEmissionsTco2e = emissions amount, example 214.5
     * request.certificatePriceEurPerTco2e = EUR carbon price, example 76
     *
     * Example input values:
     * embeddedEmissionsTco2e = 214.5
     * certificatePriceEurPerTco2e = 76
     *
     * Example output values:
     * estimatedCostEur = 16302.00
     *
     * Formula used:
     * estimatedCostEur = embeddedEmissionsTco2e x certificatePriceEurPerTco2e
     *
     * Step-by-step example calculation:
     * 1. Embedded emissions = 214.5 tCO2e
     * 2. Certificate price = 76 EUR/tCO2e
     * 3. Multiply:
     *    214.5 x 76 = 16302 EUR
     *
     * Output example:
     * {
     *   "estimatedCostEur": 16302.00
     * }
     */
    public SimpleCostResponse calculateSimpleCost(SimpleCostRequest request) {
        BigDecimal estimatedCost = request.getEmbeddedEmissionsTco2e()
                .multiply(request.getCertificatePriceEurPerTco2e());

        SimpleCostResponse response = new SimpleCostResponse();
        response.setEmbeddedEmissionsTco2e(CalculationSupport.roundEmissions(request.getEmbeddedEmissionsTco2e()));
        response.setCertificatePriceEurPerTco2e(request.getCertificatePriceEurPerTco2e());
        response.setEstimatedCostEur(CalculationSupport.roundMoney(estimatedCost));
        response.setFormula("estimatedCostEur = embeddedEmissionsTco2e × certificatePriceEurPerTco2e");
        return response;
    }

    /**
     * Compares default-value cost with actual-data cost and computes savings.
     *
     * Business meaning:
     * Calculates two costs with the same export volume and carbon price, then
     * shows the EUR difference and percentage savings from using actual data.
     *
     * Parameters:
     * request.defaultSpecificEmissionsTco2ePerTon = default emission intensity, example 2.145
     * request.actualSpecificEmissionsTco2ePerTon = actual emission intensity, example 1.6
     * request.exportVolumeTons = quantity, example 100
     * request.certificatePriceEurPerTco2e = price, example 76
     *
     * Example output values:
     * defaultCostEur = 16302.00
     * actualCostEur = 12160.00
     * potentialSavingsEur = 4142.00
     * savingsPercent = 25.41
     *
     * Formula used:
     * defaultCost = defaultSpecificEmissions x exportVolume x certificatePrice
     * actualCost = actualSpecificEmissions x exportVolume x certificatePrice
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
        BigDecimal defaultCost = request.getDefaultSpecificEmissionsTco2ePerTon()
                .multiply(request.getExportVolumeTons())
                .multiply(request.getCertificatePriceEurPerTco2e());
        BigDecimal actualCost = request.getActualSpecificEmissionsTco2ePerTon()
                .multiply(request.getExportVolumeTons())
                .multiply(request.getCertificatePriceEurPerTco2e());
        BigDecimal savings = defaultCost.subtract(actualCost);
        BigDecimal savingsPercent = defaultCost.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : savings.divide(defaultCost, 8, CalculationSupport.ROUNDING_MODE).multiply(ONE_HUNDRED);

        CompareDefaultVsActualResponse response = new CompareDefaultVsActualResponse();
        response.setDefaultCostEur(CalculationSupport.roundMoney(defaultCost));
        response.setActualCostEur(CalculationSupport.roundMoney(actualCost));
        response.setPotentialSavingsEur(CalculationSupport.roundMoney(savings));
        response.setSavingsPercent(CalculationSupport.roundPercent(savingsPercent));
        response.setMessage("Using actual emissions data instead of default values may reduce estimated CBAM exposure.");
        return response;
    }
}
