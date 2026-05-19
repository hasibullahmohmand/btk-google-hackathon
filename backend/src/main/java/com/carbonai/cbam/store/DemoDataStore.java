package com.carbonai.cbam.store;

import com.carbonai.cbam.model.CbamDefaultValue;
import com.carbonai.cbam.model.EmissionFactor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Simple in-memory demo store used instead of a database for the MVP.
 *
 * Beginner-friendly explanation:
 * Think of this class as a small in-memory catalogue. It stores the demo
 * default values and emission factors that the services need for deterministic
 * calculations. Because this MVP does not use a database yet, the data lives
 * in normal Java lists.
 */
@Component
public class DemoDataStore {

    private List<CbamDefaultValue> defaultValues = new ArrayList<>();
    private List<EmissionFactor> emissionFactors = new ArrayList<>();
    private List<BigDecimal> demoCarbonPrices = new ArrayList<>();
    private List<String> demoProducts = new ArrayList<>();

    /**
     * Finds a seeded default-value record by country and CN code.
     *
     * Business meaning:
     * This lookup is used when a company does not have actual emissions data
     * and needs to use a default emissions intensity instead.
     *
     * Example input:
     * country = "Turkey"
     * cnCode = "25233000"
     *
     * Example output:
     * a record containing 2026 default = 2.145 tCO2e/t
     */
    public Optional<CbamDefaultValue> findDefaultValue(String country, String cnCode) {
        return defaultValues.stream()
                .filter(value -> normalize(value.getCountry()).equals(normalize(country))
                        && normalize(value.getCnCode()).equals(normalize(cnCode)))
                .findFirst();
    }

    /**
     * Finds an emission factor by activity type and unit.
     *
     * Business meaning:
     * The actual emissions endpoint cannot calculate emissions unless it knows
     * how much CO2e is associated with one unit of a given activity.
     *
     * Example input:
     * activityType = "Natural gas"
     * unit = "t"
     *
     * Example output:
     * factor = derived kgCO2e/t value for the CSV-backed activity row
     */
    public Optional<EmissionFactor> findEmissionFactor(String activityType, String unit) {
        return emissionFactors.stream()
                .filter(value -> normalize(value.getActivityType()).equals(normalize(activityType))
                        && normalize(value.getUnit()).equals(normalize(unit)))
                .findFirst();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

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
