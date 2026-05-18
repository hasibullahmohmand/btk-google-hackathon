package com.carbonai.cbam.service;

import com.carbonai.cbam.dto.ActualEmissionsRequest;
import com.carbonai.cbam.dto.ActualEmissionsResponse;
import com.carbonai.cbam.exception.BusinessException;
import com.carbonai.cbam.model.ActivityEmissionBreakdown;
import com.carbonai.cbam.model.ActivityInput;
import com.carbonai.cbam.model.EmissionFactor;
import com.carbonai.cbam.store.DemoDataStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service that converts activity data into actual emissions.
 *
 * Beginner-friendly explanation:
 * This service is for the case where a factory knows what it actually used,
 * such as natural gas, diesel, and electricity. Instead of relying on default
 * values, it calculates emissions from the real activity data row by row.
 */
@Service
public class ActualEmissionCalculationService {

    private static final BigDecimal KG_PER_TON = new BigDecimal("1000");
    private final DemoDataStore demoDataStore;

    public ActualEmissionCalculationService(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    /**
     * Calculates embedded emissions from real factory activity data.
     *
     * Business meaning:
     * 1. Looks up a seeded emission factor for each activity type and unit.
     * 2. Converts activity amounts into kgCO2e.
     * 3. Converts kgCO2e into tCO2e.
     * 4. Splits emissions into direct and indirect categories.
     * 5. Computes specific emissions per ton of product.
     * 6. Computes emissions for only the exported share of production.
     *
     * Parameters:
     * request.product = product label, example "steel"
     * request.productionVolumeTons = total production, example 100
     * request.exportVolumeTons = exported quantity, example 40
     * request.includeIndirectEmissions = compatibility flag retained by the API
     * request.activities = activity rows such as natural gas, diesel, electricity
     *
     * Example input values:
     * NATURAL_GAS = 5000 m3
     * DIESEL = 1200 liter
     * ELECTRICITY = 25000 kWh
     *
     * Example output values:
     * directEmissionsTco2e = 13.2160
     * indirectEmissionsTco2e = 10.5000
     * totalFacilityEmissionsTco2e = 23.7160
     * specificEmissionsTco2ePerTon = 0.2372
     * exportedEmbeddedEmissionsTco2e = 9.4864
     *
     * Formula used:
     * activityEmissionsKg = amount x factorKgCo2ePerUnit
     * activityEmissionsTco2e = activityEmissionsKg / 1000
     * specificEmissions = totalFacilityEmissions / productionVolumeTons
     * exportedEmbeddedEmissions = specificEmissions x exportVolumeTons
     *
     * Step-by-step example calculation:
     * 1. Natural gas:
     *    5000 x 2.0 = 10000 kgCO2e = 10.0 tCO2e
     * 2. Diesel:
     *    1200 x 2.68 = 3216 kgCO2e = 3.216 tCO2e
     * 3. Electricity:
     *    25000 x 0.42 = 10500 kgCO2e = 10.5 tCO2e
     * 4. Direct emissions:
     *    10.0 + 3.216 = 13.216 tCO2e
     * 5. Indirect emissions:
     *    10.5 tCO2e
     * 6. Total facility emissions:
     *    13.216 + 10.5 = 23.716 tCO2e
     * 7. Specific emissions per ton:
     *    23.716 / 100 = 0.23716 tCO2e/t
     * 8. Exported embedded emissions:
     *    0.23716 x 40 = 9.4864 tCO2e
     *
     * Output example:
     * {
     *   "specificEmissionsTco2ePerTon": 0.2372,
     *   "exportedEmbeddedEmissionsTco2e": 9.4864,
     *   "calculationMode": "ACTUAL_DATA"
     * }
     */
    public ActualEmissionsResponse calculateActualEmissions(ActualEmissionsRequest request) {
        BigDecimal directEmissions = BigDecimal.ZERO;
        BigDecimal indirectEmissions = BigDecimal.ZERO;
        List<ActivityEmissionBreakdown> breakdownList = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // This is a business warning instead of a hard validation error because
        // some demo or edge-case scenarios may still want to see the result.
        if (request.getExportVolumeTons().compareTo(request.getProductionVolumeTons()) > 0) {
            warnings.add("exportVolumeTons is greater than productionVolumeTons. Calculation is allowed, but the inputs should be reviewed.");
        }
        if (Boolean.FALSE.equals(request.getIncludeIndirectEmissions())) {
            warnings.add("includeIndirectEmissions=false is ignored. CBAM embedded emissions include both direct and indirect emissions based on the project source markdown.");
        }

        for (ActivityInput activity : request.getActivities()) {
            EmissionFactor factor = demoDataStore.findEmissionFactor(activity.getActivityType(), activity.getUnit())
                    .orElseThrow(() -> new BusinessException(
                            "EMISSION_FACTOR_NOT_FOUND",
                            "No emission factor found for activityType=" + activity.getActivityType() + " and unit=" + activity.getUnit()
                    ));

            // Step 1: convert physical activity into kilograms of CO2 equivalent.
            BigDecimal activityEmissionsKg = activity.getAmount().multiply(factor.getFactorKgCo2ePerUnit());
            // Step 2: convert kilograms into tonnes because CBAM calculations are
            // usually presented in tCO2e, not kgCO2e.
            BigDecimal activityEmissionsTco2e = activityEmissionsKg.divide(KG_PER_TON, 8, CalculationSupport.ROUNDING_MODE);

            ActivityEmissionBreakdown breakdown = new ActivityEmissionBreakdown();
            breakdown.setActivityType(activity.getActivityType());
            breakdown.setAmount(activity.getAmount());
            breakdown.setUnit(activity.getUnit());
            breakdown.setFactor(factor.getFactorKgCo2ePerUnit());
            breakdown.setFactorUnit(factor.getFactorUnit());
            breakdown.setEmissionsTco2e(CalculationSupport.roundEmissions(activityEmissionsTco2e));

            if ("ELECTRICITY".equalsIgnoreCase(activity.getActivityType())) {
                indirectEmissions = indirectEmissions.add(activityEmissionsTco2e);
                breakdown.setEmissionCategory("INDIRECT");
            } else {
                directEmissions = directEmissions.add(activityEmissionsTco2e);
                breakdown.setEmissionCategory("DIRECT");
            }

            breakdownList.add(breakdown);
        }

        // CBAM embedded emissions include both direct and indirect emissions.
        BigDecimal totalFacilityEmissions = directEmissions.add(indirectEmissions);

        // Specific emissions tell us how many tonnes of CO2e were emitted per
        // one ton of product produced at the facility.
        BigDecimal specificEmissions = totalFacilityEmissions.divide(request.getProductionVolumeTons(), 8, CalculationSupport.ROUNDING_MODE);
        // Exported embedded emissions take that per-ton intensity and apply it
        // only to the exported quantity.
        BigDecimal exportedEmbeddedEmissions = specificEmissions.multiply(request.getExportVolumeTons());

        ActualEmissionsResponse response = new ActualEmissionsResponse();
        response.setProduct(request.getProduct());
        response.setProductionVolumeTons(request.getProductionVolumeTons());
        response.setExportVolumeTons(request.getExportVolumeTons());
        response.setDirectEmissionsTco2e(CalculationSupport.roundEmissions(directEmissions));
        response.setIndirectEmissionsTco2e(CalculationSupport.roundEmissions(indirectEmissions));
        response.setTotalFacilityEmissionsTco2e(CalculationSupport.roundEmissions(totalFacilityEmissions));
        response.setSpecificEmissionsTco2ePerTon(CalculationSupport.roundEmissions(specificEmissions));
        response.setExportedEmbeddedEmissionsTco2e(CalculationSupport.roundEmissions(exportedEmbeddedEmissions));
        response.setIncludeIndirectEmissions(Boolean.TRUE);
        response.setCalculationMode("ACTUAL_DATA");
        response.setActivityBreakdown(breakdownList);
        response.setWarnings(warnings);
        return response;
    }
}
