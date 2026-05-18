package com.carbonai.cbam.controller;

import com.carbonai.cbam.dto.ActualEmissionsRequest;
import com.carbonai.cbam.dto.ActualEmissionsResponse;
import com.carbonai.cbam.dto.AdvancedCertificatesRequest;
import com.carbonai.cbam.dto.AdvancedCertificatesResponse;
import com.carbonai.cbam.dto.CarbonPriceScenariosRequest;
import com.carbonai.cbam.dto.CarbonPriceScenariosResponse;
import com.carbonai.cbam.dto.CompareDefaultVsActualRequest;
import com.carbonai.cbam.dto.CompareDefaultVsActualResponse;
import com.carbonai.cbam.dto.DefaultEmissionsRequest;
import com.carbonai.cbam.dto.DefaultEmissionsResponse;
import com.carbonai.cbam.dto.DemoDataResponse;
import com.carbonai.cbam.dto.SimpleCostRequest;
import com.carbonai.cbam.dto.SimpleCostResponse;
import com.carbonai.cbam.dto.ValidateReportRequest;
import com.carbonai.cbam.dto.ValidateReportResponse;
import com.carbonai.cbam.service.ActualEmissionCalculationService;
import com.carbonai.cbam.service.AdvancedCertificateService;
import com.carbonai.cbam.service.CbamCostService;
import com.carbonai.cbam.service.DefaultValueService;
import com.carbonai.cbam.service.DemoDataService;
import com.carbonai.cbam.service.ReportValidationService;
import com.carbonai.cbam.service.ScenarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for the deterministic CBAM calculator engine.
 *
 * Beginner-friendly explanation:
 * A controller is the part of a Spring Boot application that receives HTTP
 * requests. It reads the request body, delegates the business calculation to a
 * service class, and returns the JSON response.
 *
 * Every endpoint here is designed so it can be called:
 * - manually by a frontend
 * - programmatically by a workflow
 * - automatically by a future AI/RAG layer as a deterministic tool
 *
 * Important note:
 * This controller intentionally does not contain the formulas themselves.
 * Keeping formulas in service classes makes the code easier to read, test, and maintain.
 */
@RestController
@RequestMapping("/api/cbam")
public class CbamCalculatorController {

    private final DefaultValueService defaultValueService;
    private final ActualEmissionCalculationService actualEmissionCalculationService;
    private final CbamCostService cbamCostService;
    private final AdvancedCertificateService advancedCertificateService;
    private final ScenarioService scenarioService;
    private final ReportValidationService reportValidationService;
    private final DemoDataService demoDataService;

    public CbamCalculatorController(DefaultValueService defaultValueService,
                                    ActualEmissionCalculationService actualEmissionCalculationService,
                                    CbamCostService cbamCostService,
                                    AdvancedCertificateService advancedCertificateService,
                                    ScenarioService scenarioService,
                                    ReportValidationService reportValidationService,
                                    DemoDataService demoDataService) {
        this.defaultValueService = defaultValueService;
        this.actualEmissionCalculationService = actualEmissionCalculationService;
        this.cbamCostService = cbamCostService;
        this.advancedCertificateService = advancedCertificateService;
        this.scenarioService = scenarioService;
        this.reportValidationService = reportValidationService;
        this.demoDataService = demoDataService;
    }

    /**
     * Calculates embedded emissions using seeded CBAM default values.
     *
     * Business meaning:
     * Uses default country + CN-code values when a company does not have actual
     * measured factory emissions data.
     *
     * Parameters:
     * request.country = exporting country, example "Turkey"
     * request.cnCode = customs CN code, example "25233000"
     * request.year = CBAM year, example 2026
     * request.exportVolumeTons = shipped quantity in tons, example 100
     *
     * Formula:
     * embeddedEmissions = exportVolumeTons x defaultValueTco2ePerTon
     *
     * Step-by-step example calculation:
     * 100 tons x 2.145 tCO2e/t = 214.5 tCO2e
     *
     * Output example:
     * {
     *   "embeddedEmissionsTco2e": 214.5000
     * }
     */
    @Operation(summary = "Calculate embedded emissions using default CBAM values")
    @PostMapping("/default-emissions")
    public DefaultEmissionsResponse calculateDefaultEmissions(@Valid @RequestBody DefaultEmissionsRequest request) {
        return defaultValueService.calculateDefaultEmissions(request);
    }

    /**
     * Calculates embedded emissions from real activity data.
     *
     * Business meaning:
     * Converts fuel and electricity activity records into total emissions, then
     * derives specific emissions per ton and exported embedded emissions.
     * Embedded emissions are calculated with both direct and indirect emissions.
     *
     * Parameters:
     * request.product = product label, example "steel"
     * request.productionVolumeTons = total produced quantity, example 100
     * request.exportVolumeTons = exported quantity, example 40
     * request.includeIndirectEmissions = backward-compatible request flag retained by the API
     * request.activities = list of factory activities, example natural gas, diesel, electricity
     *
     * Formula:
     * activityEmissionsKg = amount x factor
     * activityEmissionsTco2e = activityEmissionsKg / 1000
     * specificEmissions = totalFacilityEmissions / productionVolumeTons
     * exportedEmbeddedEmissions = specificEmissions x exportVolumeTons
     *
     * Step-by-step example calculation:
     * 23.716 total tCO2e / 100 tons = 0.23716 tCO2e/t
     * 0.23716 x 40 = 9.4864 tCO2e
     *
     * Output example:
     * {
     *   "specificEmissionsTco2ePerTon": 0.2372,
     *   "exportedEmbeddedEmissionsTco2e": 9.4864
     * }
     */
    @Operation(summary = "Calculate actual emissions from factory activity data")
    @PostMapping("/actual-emissions")
    public ActualEmissionsResponse calculateActualEmissions(@Valid @RequestBody ActualEmissionsRequest request) {
        return actualEmissionCalculationService.calculateActualEmissions(request);
    }

    /**
     * Calculates a simple financial exposure estimate.
     *
     * Business meaning:
     * Multiplies embedded emissions by the certificate price to give a simple
     * estimated EUR exposure.
     *
     * Parameters:
     * request.embeddedEmissionsTco2e = emissions in tonnes CO2e, example 214.5
     * request.certificatePriceEurPerTco2e = carbon price in EUR, example 76
     *
     * Formula:
     * estimatedCostEur = embeddedEmissionsTco2e x certificatePriceEurPerTco2e
     *
     * Step-by-step example calculation:
     * 214.5 x 76 = 16302 EUR
     *
     * Output example:
     * {
     *   "estimatedCostEur": 16302.00
     * }
     */
    @Operation(summary = "Calculate simple estimated CBAM cost")
    @PostMapping("/simple-cost")
    public SimpleCostResponse calculateSimpleCost(@Valid @RequestBody SimpleCostRequest request) {
        return cbamCostService.calculateSimpleCost(request);
    }

    /**
     * Calculates advanced CBAM certificate requirements.
     *
     * Business meaning:
     * Applies benchmark deductions, free allowance deduction factors, and third-country
     * carbon price deductions before estimating certificates and cost.
     *
     * Parameters:
     * request.actualSpecificEmissionsTco2ePerTon = A, example 2.145
     * request.cbamBenchmarkTco2ePerTon = benchmark, example 1.5
     * request.cbamFactor = free allowance factor, example 0.975
     * request.thirdCountryCarbonPriceEurPerTco2e = non-EU price, example 0
     * request.cbamCertificatePriceEurPerTco2e = EU certificate price, example 76
     * request.importedQuantityTons = D, example 100
     *
     * Formula:
     * certificates = max(0, (A - B - C) x D)
     *
     * Step-by-step example calculation:
     * certificates = 68.25
     * cost = 5187 EUR
     *
     * Output example:
     * {
     *   "certificatesToSurrender": 68.2500,
     *   "estimatedCostEur": 5187.00
     * }
     */
    @Operation(summary = "Calculate advanced CBAM certificates and cost")
    @PostMapping("/advanced-certificates")
    public AdvancedCertificatesResponse calculateAdvancedCertificates(@Valid @RequestBody AdvancedCertificatesRequest request) {
        return advancedCertificateService.calculateAdvancedCertificates(request);
    }

    /**
     * Compares default-value cost with actual-data cost.
     *
     * Business meaning:
     * Helps exporters understand the potential financial savings of using measured
     * actual emissions instead of higher default values.
     *
     * Parameters:
     * request.defaultSpecificEmissionsTco2ePerTon = default emission intensity, example 2.145
     * request.actualSpecificEmissionsTco2ePerTon = actual emission intensity, example 1.6
     * request.exportVolumeTons = export quantity, example 100
     * request.certificatePriceEurPerTco2e = carbon price, example 76
     *
     * Formula:
     * defaultCost = defaultSpecificEmissions x exportVolume x certificatePrice
     * actualCost = actualSpecificEmissions x exportVolume x certificatePrice
     * savings = defaultCost - actualCost
     *
     * Step-by-step example calculation:
     * defaultCost = 2.145 x 100 x 76 = 16302
     * actualCost = 1.6 x 100 x 76 = 12160
     * savings = 4142
     */
    @Operation(summary = "Compare default-value and actual-emissions costs")
    @PostMapping("/compare-default-vs-actual")
    public CompareDefaultVsActualResponse compareDefaultVsActual(@Valid @RequestBody CompareDefaultVsActualRequest request) {
        return cbamCostService.compareDefaultVsActual(request);
    }

    /**
     * Runs carbon price scenarios against a fixed emissions quantity.
     *
     * Business meaning:
     * Calculates several possible EUR exposures for multiple future carbon prices.
     *
     * Parameters:
     * request.embeddedEmissionsTco2e = emissions amount, example 214.5
     * request.pricesEurPerTco2e = list of prices, example [76, 100, 120]
     *
     * Formula:
     * scenarioCost = embeddedEmissionsTco2e x price
     *
     * Step-by-step example calculation:
     * 214.5 x 100 = 21450 EUR
     */
    @Operation(summary = "Run carbon price scenario analysis")
    @PostMapping("/scenarios")
    public CarbonPriceScenariosResponse runScenarios(@Valid @RequestBody CarbonPriceScenariosRequest request) {
        return scenarioService.calculateScenarios(request);
    }

    /**
     * Validates a CBAM-style report payload for structural and consistency rules.
     *
     * Business meaning:
     * Checks the internal structure of a report before export or submission.
     * It does not submit anything to an official CBAM registry.
     *
     * Parameters:
     * request.goodsItemNumber = line item id, example "1"
     * request.sequenceNumber = sequence id, example "1"
     * request.cnCode = CN code, example "25233000"
     * request.country = country, example "Turkey"
     * request.period = period, example "2026"
     * request.directEmissionsTco2e = direct emissions, example 1.82
     * request.indirectEmissionsTco2e = indirect emissions, example 0.14
     * request.totalEmissionsTco2e = total emissions, example 1.96
     * request.netMassTons = quantity, example 100
     *
     * Formula:
     * totalEmissions should equal directEmissions + indirectEmissions within tolerance 0.0001
     *
     * Step-by-step example calculation:
     * expectedTotal = 1.82 + 0.14 = 1.96
     * if the provided total is 1.95, validation rule R0010 fails
     */
    @Operation(summary = "Validate a generated CBAM-ready report")
    @PostMapping("/validate-report")
    public ValidateReportResponse validateReport(@Valid @RequestBody ValidateReportRequest request) {
        return reportValidationService.validateReport(request);
    }

    /**
     * Returns all seeded demo values.
     *
     * Business meaning:
     * Helps the frontend and AI tool layer discover the available demo defaults,
     * emission factors, products, and demo price scenarios.
     *
     * Input example:
     * no request body
     *
     * Formula:
     * no mathematical formula is used because this endpoint only returns demo values
     *
     * Output example:
     * defaultValues, emissionFactors, demoCarbonPrices, demoProducts
     */
    @Operation(summary = "Return all seeded demo data")
    @GetMapping("/demo-data")
    public DemoDataResponse getDemoData() {
        return demoDataService.getDemoData();
    }
}
