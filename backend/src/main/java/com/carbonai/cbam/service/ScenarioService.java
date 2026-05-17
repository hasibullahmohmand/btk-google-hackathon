package com.carbonai.cbam.service;

import com.carbonai.cbam.dto.CarbonPriceScenariosRequest;
import com.carbonai.cbam.dto.CarbonPriceScenariosResponse;
import com.carbonai.cbam.model.ScenarioResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for scenario calculations.
 *
 * Beginner-friendly explanation:
 * This service answers "what if the carbon price changes?" It does not change
 * the emissions amount. It only reruns the cost estimate against multiple
 * possible prices.
 */
@Service
public class ScenarioService {

    /**
     * Calculates carbon price scenarios for a fixed emissions quantity.
     *
     * Business meaning:
     * Runs the same emissions quantity across multiple certificate prices so
     * users can estimate best-case and worst-case exposure scenarios.
     *
     * Parameters:
     * request.embeddedEmissionsTco2e = emissions quantity, example 214.5
     * request.pricesEurPerTco2e = price list, example [76, 100, 120]
     *
     * Example output values:
     * 76 -> 16302.00
     * 100 -> 21450.00
     * 120 -> 25740.00
     *
     * Formula used:
     * scenarioCost = embeddedEmissionsTco2e x priceEurPerTco2e
     *
     * Step-by-step example calculation:
     * 1. Embedded emissions = 214.5 tCO2e
     * 2. Price scenario A = 76 -> 214.5 x 76 = 16302
     * 3. Price scenario B = 100 -> 214.5 x 100 = 21450
     * 4. Price scenario C = 120 -> 214.5 x 120 = 25740
     *
     * Output example:
     * {
     *   "scenarios": [
     *     { "priceEurPerTco2e": 76, "estimatedCostEur": 16302.00 }
     *   ]
     * }
     */
    public CarbonPriceScenariosResponse calculateScenarios(CarbonPriceScenariosRequest request) {
        List<ScenarioResult> scenarioResults = request.getPricesEurPerTco2e()
                .stream()
                .map(price -> {
                    ScenarioResult result = new ScenarioResult();
                    result.setPriceEurPerTco2e(price);
                    result.setEstimatedCostEur(CalculationSupport.roundMoney(request.getEmbeddedEmissionsTco2e().multiply(price)));
                    return result;
                })
                .toList();

        CarbonPriceScenariosResponse response = new CarbonPriceScenariosResponse();
        response.setEmbeddedEmissionsTco2e(CalculationSupport.roundEmissions(request.getEmbeddedEmissionsTco2e()));
        response.setScenarios(scenarioResults);
        return response;
    }
}
