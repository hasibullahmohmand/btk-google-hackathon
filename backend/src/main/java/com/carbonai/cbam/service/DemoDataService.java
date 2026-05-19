package com.carbonai.cbam.service;

import com.carbonai.cbam.dto.DemoDataResponse;
import com.carbonai.cbam.store.CsvDefaultValueRepository;
import com.carbonai.cbam.store.DemoDataStore;
import com.carbonai.cbam.store.EmissionTableRepository;
import org.springframework.stereotype.Service;

/**
 * Service that returns all seeded demo values.
 *
 * Beginner-friendly explanation:
 * This service is not a calculator. It is a discovery endpoint that tells the
 * frontend or AI tool layer which demo values are already available.
 */
@Service
public class DemoDataService {

    private final DemoDataStore demoDataStore;
    private final CsvDefaultValueRepository csvDefaultValueRepository;
    private final EmissionTableRepository emissionTableRepository;

    public DemoDataService(DemoDataStore demoDataStore,
                           CsvDefaultValueRepository csvDefaultValueRepository,
                           EmissionTableRepository emissionTableRepository) {
        this.demoDataStore = demoDataStore;
        this.csvDefaultValueRepository = csvDefaultValueRepository;
        this.emissionTableRepository = emissionTableRepository;
    }

    /**
     * Returns all seeded demo data for discovery and integration purposes.
     *
     * Business meaning:
     * Exposes the in-memory default values, emission factors, demo prices,
     * and demo products to the frontend and the future AI tool layer.
     *
     * Input example:
     * no request body is required
     *
     * Formula:
     * no mathematical formula is used here because this is a data retrieval method
     *
     * Step-by-step example:
     * 1. Read the seeded default values list
     * 2. Read the seeded emission factors list
     * 3. Read the demo carbon prices
     * 4. Return everything in a single response object
     *
     * Example output values:
     * defaultValues = Turkey cement demo record
     * emissionFactors = rows loaded from emission_tables_csv
     * demoCarbonPrices = [76, 100, 120]
     * demoProducts = ["Aluminous cement", "Steel", ...]
     */
    public DemoDataResponse getDemoData() {
        DemoDataResponse response = new DemoDataResponse();
        response.setDefaultValues(csvDefaultValueRepository.getAllDefaultValues());
        response.setEmissionFactors(emissionTableRepository.getAllFactors());
        response.setDemoCarbonPrices(demoDataStore.getDemoCarbonPrices());
        response.setDemoProducts(demoDataStore.getDemoProducts());
        return response;
    }
}
