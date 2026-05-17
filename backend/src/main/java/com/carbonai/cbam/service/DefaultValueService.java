package com.carbonai.cbam.service;

import com.carbonai.cbam.dto.DefaultEmissionsRequest;
import com.carbonai.cbam.dto.DefaultEmissionsResponse;
import com.carbonai.cbam.exception.BusinessException;
import com.carbonai.cbam.model.CbamDefaultValue;
import com.carbonai.cbam.store.DemoDataStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for default-value emissions calculations.
 *
 * Beginner-friendly explanation:
 * This service answers a simple question:
 * "If I do not have real factory emissions data, how many embedded emissions
 * should I estimate by using the default CBAM value for my product?"
 */
@Service
public class DefaultValueService {

    private final DemoDataStore demoDataStore;

    public DefaultValueService(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    /**
     * Calculates embedded emissions using CBAM default values.
     *
     * Business meaning:
     * Uses a seeded default emissions value for a country and CN code when the
     * exporter does not have actual factory emissions data.
     *
     * Parameters:
     * request.country = exporting country, example "Turkey"
     * request.cnCode = CN code, example "25233000"
     * request.year = target year, example 2026
     * request.exportVolumeTons = exported quantity, example 100
     *
     * Example input values:
     * country = "Turkey"
     * cnCode = "25233000"
     * year = 2026
     * exportVolumeTons = 100
     *
     * Example output values:
     * selectedDefaultValueTco2ePerTon = 2.1450
     * embeddedEmissionsTco2e = 214.5000
     *
     * Formula used:
     * embeddedEmissions = exportVolumeTons x selectedDefaultValueTco2ePerTon
     *
     * Step-by-step example calculation:
     * 100 x 2.145 = 214.5 tCO2e
     *
     * Output example:
     * {
     *   "selectedDefaultValueTco2ePerTon": 2.1450,
     *   "embeddedEmissionsTco2e": 214.5000,
     *   "calculationMode": "DEFAULT_VALUE"
     * }
     */
    public DefaultEmissionsResponse calculateDefaultEmissions(DefaultEmissionsRequest request) {
        CbamDefaultValue defaultValue = demoDataStore.findDefaultValue(request.getCountry(), request.getCnCode())
                .orElseThrow(() -> new BusinessException(
                        "DEFAULT_VALUE_NOT_FOUND",
                        "No CBAM default value found for country=" + request.getCountry() + " and cnCode=" + request.getCnCode()
                ));

        BigDecimal selectedDefaultValue = selectDefaultValueByYear(defaultValue, request.getYear());
        BigDecimal embeddedEmissions = request.getExportVolumeTons().multiply(selectedDefaultValue);

        DefaultEmissionsResponse response = new DefaultEmissionsResponse();
        response.setCountry(defaultValue.getCountry());
        response.setCnCode(defaultValue.getCnCode());
        response.setProductDescription(defaultValue.getProductDescription());
        response.setYear(request.getYear());
        response.setExportVolumeTons(request.getExportVolumeTons());
        response.setSelectedDefaultValueTco2ePerTon(CalculationSupport.roundEmissions(selectedDefaultValue));
        response.setEmbeddedEmissionsTco2e(CalculationSupport.roundEmissions(embeddedEmissions));
        response.setCalculationMode("DEFAULT_VALUE");
        response.setFormula("embeddedEmissions = exportVolumeTons × defaultValueTco2ePerTon");
        return response;
    }

    /**
     * Selects the seeded default value based on the requested year.
     *
     * Business meaning:
     * Different years can use different fallback intensities. This helper keeps
     * that year-selection logic out of the controller.
     *
     * Parameters:
     * defaultValue = seeded default value record
     * year = requested year, example 2026
     *
     * Example:
     * year 2026 -> default2026WithMarkup
     * year 2027 -> default2027WithMarkup
     * year 2028 or later -> default2028OnwardsWithMarkup
     *
     * Output example:
     * returns the relevant tCO2e per ton default value
     */
    BigDecimal selectDefaultValueByYear(CbamDefaultValue defaultValue, Integer year) {
        if (year == 2026) {
            return defaultValue.getDefault2026WithMarkup();
        }
        if (year == 2027) {
            return defaultValue.getDefault2027WithMarkup();
        }
        return defaultValue.getDefault2028OnwardsWithMarkup();
    }
}
