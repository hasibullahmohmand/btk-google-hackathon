package com.carbonai.cbam.config;

import com.carbonai.cbam.model.CbamDefaultValue;
import com.carbonai.cbam.store.DemoDataStore;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds lightweight demo data that is unrelated to the CSV emission tables.
 *
 * Emission factors are now loaded from emission_tables_csv through
 * EmissionTableRepository, so this class only keeps the legacy demo values that
 * still help the UI discover products and sample prices.
 */
@Component
public class SeedDataConfig {

    private final DemoDataStore demoDataStore;

    public SeedDataConfig(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    @PostConstruct
    public void seedData() {
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

        demoDataStore.setEmissionFactors(List.of());
        demoDataStore.setDemoCarbonPrices(List.of(
                new BigDecimal("76"),
                new BigDecimal("100"),
                new BigDecimal("120")
        ));
        demoDataStore.setDemoProducts(List.of(
                "Aluminous cement",
                "Steel",
                "Aluminium",
                "Fertiliser",
                "Hydrogen"
        ));
    }
}
