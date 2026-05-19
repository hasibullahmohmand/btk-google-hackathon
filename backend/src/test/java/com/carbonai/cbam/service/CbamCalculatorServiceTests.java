package com.carbonai.cbam.service;

import com.carbonai.cbam.config.SeedDataConfig;
import com.carbonai.cbam.dto.ActualEmissionsRequest;
import com.carbonai.cbam.dto.AdvancedCertificatesRequest;
import com.carbonai.cbam.dto.CompareDefaultVsActualRequest;
import com.carbonai.cbam.dto.DefaultEmissionsRequest;
import com.carbonai.cbam.dto.ValidateReportRequest;
import com.carbonai.cbam.model.ActivityInput;
import com.carbonai.cbam.store.CsvDefaultValueRepository;
import com.carbonai.cbam.store.DemoDataStore;
import com.carbonai.cbam.store.EmissionTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CbamCalculatorServiceTests {

    private DefaultValueService defaultValueService;
    private ActualEmissionCalculationService actualEmissionCalculationService;
    private CbamCostService cbamCostService;
    private AdvancedCertificateService advancedCertificateService;
    private ReportValidationService reportValidationService;

    @BeforeEach
    void setUp() {
        DemoDataStore demoDataStore = new DemoDataStore();
        new SeedDataConfig(demoDataStore).seedData();
        EmissionTableRepository emissionTableRepository = new EmissionTableRepository();
        emissionTableRepository.load();
        CsvDefaultValueRepository csvDefaultValueRepository = new CsvDefaultValueRepository();
        csvDefaultValueRepository.load();
        defaultValueService = new DefaultValueService(csvDefaultValueRepository);
        actualEmissionCalculationService = new ActualEmissionCalculationService(emissionTableRepository);
        cbamCostService = new CbamCostService();
        advancedCertificateService = new AdvancedCertificateService();
        reportValidationService = new ReportValidationService();
    }

    @Test
    void shouldCalculateDefaultEmissionsFromCountryFile() {
        DefaultEmissionsRequest request = new DefaultEmissionsRequest();
        request.setCountry("Turkey");
        request.setCnCode("25233000");
        request.setYear(2026);
        request.setExportVolumeTons(new BigDecimal("100"));

        BigDecimal result = defaultValueService.calculateDefaultEmissions(request).getEmbeddedEmissionsTco2e();

        assertThat(result).isEqualByComparingTo("214.5000");
    }

    @Test
    void shouldCalculateDefaultEmissionsFromTransitionalFile() {
        DefaultEmissionsRequest request = new DefaultEmissionsRequest();
        request.setCountry("Turkey");
        request.setCnCode("25233000");
        request.setYear(2024);
        request.setExportVolumeTons(new BigDecimal("100"));

        BigDecimal result = defaultValueService.calculateDefaultEmissions(request).getEmbeddedEmissionsTco2e();

        assertThat(result).isEqualByComparingTo("190.0000");
    }

    @Test
    void shouldFallbackToBenchmarkFileWhenCountryValueMissing() {
        DefaultEmissionsRequest request = new DefaultEmissionsRequest();
        request.setCountry("Turkey");
        request.setCnCode("25232100");
        request.setYear(2026);
        request.setExportVolumeTons(new BigDecimal("100"));

        BigDecimal result = defaultValueService.calculateDefaultEmissions(request).getEmbeddedEmissionsTco2e();

        assertThat(result).isEqualByComparingTo("85.9000");
    }

    @Test
    void shouldCalculateActualEmissions() {
        ActualEmissionsRequest request = new ActualEmissionsRequest();
        request.setCnCode("72142000");
        request.setCountry("Turkey");
        request.setYear(2026);
        request.setProductionVolumeTons(new BigDecimal("100"));
        request.setExportVolumeTons(new BigDecimal("40"));
        request.setIncludeIndirectEmissions(true);

        ActivityInput naturalGas = new ActivityInput();
        naturalGas.setActivityType("Natural gas");
        naturalGas.setAmount(new BigDecimal("50"));
        naturalGas.setUnit("t");

        ActivityInput diesel = new ActivityInput();
        diesel.setActivityType("Gas/Diesel oil");
        diesel.setAmount(new BigDecimal("5"));
        diesel.setUnit("t");

        request.setActivities(List.of(naturalGas, diesel));

        var response = actualEmissionCalculationService.calculateActualEmissions(request);

        assertThat(response.getCnCode()).isEqualTo("72142000");
        assertThat(response.getCountry()).isEqualTo("Turkey");
        assertThat(response.getYear()).isEqualTo(2026);
        assertThat(response.getDirectEmissionsTco2e()).isEqualByComparingTo("150.5715");
        assertThat(response.getIndirectEmissionsTco2e()).isEqualByComparingTo("0.0000");
        assertThat(response.getTotalFacilityEmissionsTco2e()).isEqualByComparingTo("150.5715");
        assertThat(response.getSpecificEmissionsTco2ePerTon()).isEqualByComparingTo("1.5057");
        assertThat(response.getExportedEmbeddedEmissionsTco2e()).isEqualByComparingTo("60.2286");
    }

    @Test
    void shouldStillIncludeIndirectEmissionsWhenCompatibilityFlagIsFalse() {
        ActualEmissionsRequest request = new ActualEmissionsRequest();
        request.setCnCode("72142000");
        request.setCountry("Turkey");
        request.setYear(2026);
        request.setProductionVolumeTons(new BigDecimal("100"));
        request.setExportVolumeTons(new BigDecimal("40"));
        request.setIncludeIndirectEmissions(false);

        ActivityInput naturalGas = new ActivityInput();
        naturalGas.setActivityType("Natural gas");
        naturalGas.setAmount(new BigDecimal("50"));
        naturalGas.setUnit("t");

        ActivityInput diesel = new ActivityInput();
        diesel.setActivityType("Gas/Diesel oil");
        diesel.setAmount(new BigDecimal("5"));
        diesel.setUnit("t");

        request.setActivities(List.of(naturalGas, diesel));

        var response = actualEmissionCalculationService.calculateActualEmissions(request);

        assertThat(response.getIncludeIndirectEmissions()).isTrue();
        assertThat(response.getTotalFacilityEmissionsTco2e()).isEqualByComparingTo("150.5715");
        assertThat(response.getWarnings())
                .anyMatch(warning -> warning.contains("includeIndirectEmissions=false is ignored"));
    }

    @Test
    void shouldCalculateAdvancedCertificates() {
        AdvancedCertificatesRequest request = new AdvancedCertificatesRequest();
        request.setActualSpecificEmbeddedEmissionsTco2ePerTon(new BigDecimal("2.145"));
        request.setSpecificEmbeddedFreeAllocationTco2ePerTon(new BigDecimal("1.4625"));
        request.setEffectiveCarbonPricePaidInCountryOfOriginEurPerTco2e(BigDecimal.ZERO);
        request.setEuEtsWeeklyAveragePriceEurPerTco2e(new BigDecimal("76"));
        request.setImportedQuantityTons(new BigDecimal("100"));

        var response = advancedCertificateService.calculateAdvancedCertificates(request);

        assertThat(response.getTotalEmbeddedEmissionsTco2e()).isEqualByComparingTo("214.5000");
        assertThat(response.getCertificateSurrenderObligationBeforeCarbonPriceAdjustment()).isEqualByComparingTo("68.2500");
        assertThat(response.getCarbonPriceReductionInCertificates()).isEqualByComparingTo("0.0000");
        assertThat(response.getCertificatesToSurrender()).isEqualByComparingTo("68.2500");
        assertThat(response.getEstimatedCostEur()).isEqualByComparingTo("5187.00");
    }

    @Test
    void shouldCompareDefaultVsActual() {
        CompareDefaultVsActualRequest request = new CompareDefaultVsActualRequest();
        request.setDefaultSpecificEmbeddedEmissionsTco2ePerTon(new BigDecimal("2.145"));
        request.setActualSpecificEmbeddedEmissionsTco2ePerTon(new BigDecimal("1.6"));
        request.setExportVolumeTons(new BigDecimal("100"));
        request.setEuEtsWeeklyAveragePriceEurPerTco2e(new BigDecimal("76"));

        var response = cbamCostService.compareDefaultVsActual(request);

        assertThat(response.getDefaultCostEur()).isEqualByComparingTo("16302.00");
        assertThat(response.getActualCostEur()).isEqualByComparingTo("12160.00");
        assertThat(response.getPotentialSavingsEur()).isEqualByComparingTo("4142.00");
        assertThat(response.getSavingsPercent()).isEqualByComparingTo("25.41");
    }

    @Test
    void shouldFailR0010WhenTotalsDoNotMatch() {
        ValidateReportRequest request = new ValidateReportRequest();
        request.setGoodsItemNumber("1");
        request.setSequenceNumber("1");
        request.setCnCode("25233000");
        request.setCountry("Turkey");
        request.setPeriod("2026");
        request.setDirectEmissionsTco2e(new BigDecimal("1.82"));
        request.setIndirectEmissionsTco2e(new BigDecimal("0.14"));
        request.setTotalEmissionsTco2e(new BigDecimal("1.95"));
        request.setNetMassTons(new BigDecimal("100"));

        var response = reportValidationService.validateReport(request);

        assertThat(response.isValid()).isFalse();
        assertThat(response.getErrors()).anyMatch(error -> "R0010".equals(error.getCode()));
    }
}
