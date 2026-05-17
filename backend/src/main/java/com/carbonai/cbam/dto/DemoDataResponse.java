package com.carbonai.cbam.dto;

import com.carbonai.cbam.model.CbamDefaultValue;
import com.carbonai.cbam.model.EmissionFactor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response body for /api/cbam/demo-data.
 *
 * Beginner-friendly explanation:
 * This response bundles the demo values that are already available in the MVP.
 */
public class DemoDataResponse {

    /** Seeded default-value records. */
    private List<CbamDefaultValue> defaultValues;
    /** Seeded emission factors. */
    private List<EmissionFactor> emissionFactors;
    /** Demo carbon prices. */
    private List<BigDecimal> demoCarbonPrices;
    /** Human-readable demo product labels. */
    private List<String> demoProducts;

    public List<CbamDefaultValue> getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(List<CbamDefaultValue> defaultValues) {
        this.defaultValues = defaultValues;
    }

    public List<EmissionFactor> getEmissionFactors() {
        return emissionFactors;
    }

    public void setEmissionFactors(List<EmissionFactor> emissionFactors) {
        this.emissionFactors = emissionFactors;
    }

    public List<BigDecimal> getDemoCarbonPrices() {
        return demoCarbonPrices;
    }

    public void setDemoCarbonPrices(List<BigDecimal> demoCarbonPrices) {
        this.demoCarbonPrices = demoCarbonPrices;
    }

    public List<String> getDemoProducts() {
        return demoProducts;
    }

    public void setDemoProducts(List<String> demoProducts) {
        this.demoProducts = demoProducts;
    }
}
