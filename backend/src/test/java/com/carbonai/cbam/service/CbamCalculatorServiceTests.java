package com.carbonai.cbam.service;

import com.carbonai.cbam.config.SeedDataConfig;
import com.carbonai.cbam.dto.ActualEmissionsRequest;
import com.carbonai.cbam.dto.AdvancedCertificatesRequest;
import com.carbonai.cbam.dto.CompareDefaultVsActualRequest;
import com.carbonai.cbam.dto.DefaultEmissionsRequest;
import com.carbonai.cbam.dto.SimpleCostRequest;
import com.carbonai.cbam.dto.ValidateReportRequest;
import com.carbonai.cbam.model.ActivityInput;
import com.carbonai.cbam.store.DemoDataStore;
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
        defaultValueService = new DefaultValueService(demoDataStore);
        actualEmissionCalculationService = new ActualEmissionCalculationService(demoDataStore);
        cbamCostService = new CbamCostService();
        advancedCertificateService = new AdvancedCertificateService();
        reportValidationService = new ReportValidationService();
    }

    @Test
    void shouldCalculateDefaultEmissions() {
        DefaultEmissionsRequest request = new DefaultEmissionsRequest();
        request.setCountry("Turkey");
        request.setCnCode("25233000");
        request.setYear(2026);
        request.setExportVolumeTons(new BigDecimal("100"));

        BigDecimal result = defaultValueService.calculateDefaultEmissions(request).getEmbeddedEmissionsTco2e();

        assertThat(result).isEqualByComparingTo("214.5000");
    }

    @Test
    void shouldCalculateActualEmissions() {
        ActualEmissionsRequest request = new ActualEmissionsRequest();
        request.setProduct("steel");
        request.setProductionVolumeTons(new BigDecimal("100"));
        request.setExportVolumeTons(new BigDecimal("40"));
        request.setIncludeIndirectEmissions(true);

        ActivityInput naturalGas = new ActivityInput();
        naturalGas.setActivityType("NATURAL_GAS");
        naturalGas.setAmount(new BigDecimal("5000"));
        naturalGas.setUnit("m3");

        ActivityInput diesel = new ActivityInput();
        diesel.setActivityType("DIESEL");
        diesel.setAmount(new BigDecimal("1200"));
        diesel.setUnit("liter");

        ActivityInput electricity = new ActivityInput();
        electricity.setActivityType("ELECTRICITY");
        electricity.setAmount(new BigDecimal("25000"));
        electricity.setUnit("kWh");

        request.setActivities(List.of(naturalGas, diesel, electricity));

        var response = actualEmissionCalculationService.calculateActualEmissions(request);

        assertThat(response.getDirectEmissionsTco2e()).isEqualByComparingTo("13.2160");
        assertThat(response.getIndirectEmissionsTco2e()).isEqualByComparingTo("10.5000");
        assertThat(response.getTotalFacilityEmissionsTco2e()).isEqualByComparingTo("23.7160");
        assertThat(response.getExportedEmbeddedEmissionsTco2e()).isEqualByComparingTo("9.4864");
    }

    @Test
    void shouldCalculateSimpleCost() {
        SimpleCostRequest request = new SimpleCostRequest();
        request.setEmbeddedEmissionsTco2e(new BigDecimal("214.5"));
        request.setCertificatePriceEurPerTco2e(new BigDecimal("76"));

        BigDecimal result = cbamCostService.calculateSimpleCost(request).getEstimatedCostEur();

        assertThat(result).isEqualByComparingTo("16302.00");
    }

    @Test
    void shouldCalculateAdvancedCertificates() {
        AdvancedCertificatesRequest request = new AdvancedCertificatesRequest();
        request.setActualSpecificEmissionsTco2ePerTon(new BigDecimal("2.145"));
        request.setCbamBenchmarkTco2ePerTon(new BigDecimal("1.5"));
        request.setCbamFactor(new BigDecimal("0.975"));
        request.setThirdCountryCarbonPriceEurPerTco2e(BigDecimal.ZERO);
        request.setCbamCertificatePriceEurPerTco2e(new BigDecimal("76"));
        request.setImportedQuantityTons(new BigDecimal("100"));

        var response = advancedCertificateService.calculateAdvancedCertificates(request);

        assertThat(response.getCertificatesToSurrender()).isEqualByComparingTo("68.2500");
        assertThat(response.getEstimatedCostEur()).isEqualByComparingTo("5187.00");
    }

    @Test
    void shouldCompareDefaultVsActual() {
        CompareDefaultVsActualRequest request = new CompareDefaultVsActualRequest();
        request.setDefaultSpecificEmissionsTco2ePerTon(new BigDecimal("2.145"));
        request.setActualSpecificEmissionsTco2ePerTon(new BigDecimal("1.6"));
        request.setExportVolumeTons(new BigDecimal("100"));
        request.setCertificatePriceEurPerTco2e(new BigDecimal("76"));

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
