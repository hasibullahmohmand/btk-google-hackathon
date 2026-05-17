package com.carbonai.cbam.config;

import com.carbonai.cbam.model.CbamDefaultValue;
import com.carbonai.cbam.model.EmissionFactor;
import com.carbonai.cbam.store.DemoDataStore;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds demo data into the in-memory store used by this MVP backend.
 *
 * Why this class exists:
 * The calculator endpoints need some known values to work with even before a
 * real database is introduced. Instead of leaving the APIs empty, this class
 * creates a small, understandable dataset that the frontend and future AI tool
 * layer can call immediately.
 *
 * Business meaning of the seeded data:
 * - CBAM default values represent fallback emissions intensities used when a
 *   company does not have actual factory data.
 * - Emission factors convert a physical activity, such as burning fuel or using
 *   electricity, into kgCO2e.
 * - Demo carbon prices help scenario analysis and cost estimation.
 *
 * Seeded examples included here:
 * - Turkey + CN code 25233000 for aluminous cement
 * - NATURAL_GAS = 2.0 kgCO2e/m3
 * - DIESEL = 2.68 kgCO2e/liter
 * - ELECTRICITY = 0.42 kgCO2e/kWh
 * - Demo prices = 76, 100, 120 EUR per tCO2e
 */
@Component
public class SeedDataConfig {

    private final DemoDataStore demoDataStore;

    public SeedDataConfig(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    @PostConstruct
    public void seedData() {
        // Default values are used in "fallback mode" when the exporter does not
        // have actual measured plant-level emissions data.
        demoDataStore.setDefaultValues(List.of(
                new CbamDefaultValue(
                        "Turkey",
                        "25233000",
                        "Aluminous cement",
                        "Cement",
                        new BigDecimal("1.820"),
                        new BigDecimal("0.140"),
                        new BigDecimal("1.950"),
                        new BigDecimal("2.145"),
                        new BigDecimal("2.340"),
                        new BigDecimal("2.535")
                )
        ));

        // Emission factors convert an activity amount into kgCO2e.
        // Example:
        // 5000 m3 natural gas x 2.0 kgCO2e/m3 = 10000 kgCO2e = 10 tCO2e
        demoDataStore.setEmissionFactors(List.of(
                new EmissionFactor("NATURAL_GAS", "m3", new BigDecimal("2.0"), "kgCO2e/m3", "Demo combustion factor"),
                new EmissionFactor("DIESEL", "liter", new BigDecimal("2.68"), "kgCO2e/liter", "Demo diesel factor"),
                new EmissionFactor("ELECTRICITY", "kWh", new BigDecimal("0.42"), "kgCO2e/kWh", "Demo Turkey electricity grid factor")
        ));

        // Demo prices allow the frontend to show immediate scenario examples.
        demoDataStore.setDemoCarbonPrices(List.of(
                new BigDecimal("76"),
                new BigDecimal("100"),
                new BigDecimal("120")
        ));

        // Demo product labels are just discovery values for the MVP.
        demoDataStore.setDemoProducts(List.of(
                "Aluminous cement",
                "Steel",
                "Aluminium",
                "Fertiliser",
                "Hydrogen"
        ));
    }
}
