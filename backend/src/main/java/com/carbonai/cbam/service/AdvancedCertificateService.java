package com.carbonai.cbam.service;

import com.carbonai.cbam.dto.AdvancedCertificatesRequest;
import com.carbonai.cbam.dto.AdvancedCertificatesResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for advanced CBAM certificate calculations.
 *
 * Beginner-friendly explanation:
 * This service estimates how many CBAM certificates may need to be surrendered
 * after applying deductions. It is more detailed than the simple cost endpoint
 * because it works with actual specific emissions, benchmarks, and possible
 * carbon price deductions paid outside the EU.
 */
@Service
public class AdvancedCertificateService {

    /**
     * Calculates estimated CBAM certificates to surrender using the advanced formula.
     *
     * Business meaning:
     * Applies free allowance deduction and third-country carbon price deduction
     * before multiplying the remaining specific emissions by imported quantity.
     *
     * Parameters:
     * request.actualSpecificEmissionsTco2ePerTon = A, example 2.145
     * request.cbamBenchmarkTco2ePerTon = benchmark used for B, example 1.5
     * request.cbamFactor = free allowance factor, example 0.975
     * request.thirdCountryCarbonPriceEurPerTco2e = non-EU carbon price, example 0
     * request.cbamCertificatePriceEurPerTco2e = EU CBAM certificate price, example 76
     * request.importedQuantityTons = D, example 100
     *
     * Example output values:
     * freeAllowanceDeductionTco2ePerTon = 1.4625
     * thirdCountryCarbonPriceDeductionTco2ePerTon = 0.0000
     * certificatesToSurrender = 68.2500
     * estimatedCostEur = 5187.00
     *
     * Formula used:
     * B = cbamBenchmark x cbamFactor
     * C = (thirdCountryCarbonPrice x actualSpecificEmissions) / cbamCertificatePrice
     * certificates = max(0, (A - B - C) x D)
     * estimatedCostEur = certificates x cbamCertificatePrice
     *
     * Step-by-step example calculation:
     * 1. A = 2.145
     * 2. B = 1.5 x 0.975 = 1.4625
     * 3. C = 0 x 2.145 / 76 = 0
     * 4. Net specific emissions:
     *    2.145 - 1.4625 - 0 = 0.6825
     * 5. Certificates:
     *    0.6825 x 100 = 68.25
     * 6. Estimated cost:
     *    68.25 x 76 = 5187
     *
     * Output example:
     * {
     *   "certificatesToSurrender": 68.2500,
     *   "estimatedCostEur": 5187.00
     * }
     */
    public AdvancedCertificatesResponse calculateAdvancedCertificates(AdvancedCertificatesRequest request) {
        // B in the formula: benchmark-based deduction linked to free allowance phase-out.
        BigDecimal freeAllowanceDeduction = request.getCbamBenchmarkTco2ePerTon().multiply(request.getCbamFactor());
        // C in the formula: deduction for an already-paid third-country carbon price.
        BigDecimal thirdCountryDeduction = request.getThirdCountryCarbonPriceEurPerTco2e()
                .multiply(request.getActualSpecificEmissionsTco2ePerTon())
                .divide(request.getCbamCertificatePriceEurPerTco2e(), 8, CalculationSupport.ROUNDING_MODE);

        // Remaining specific emissions after deductions.
        BigDecimal netSpecific = request.getActualSpecificEmissionsTco2ePerTon()
                .subtract(freeAllowanceDeduction)
                .subtract(thirdCountryDeduction);

        // max(0, ...) prevents negative certificate counts.
        BigDecimal certificates = netSpecific.max(BigDecimal.ZERO).multiply(request.getImportedQuantityTons());
        BigDecimal cost = certificates.multiply(request.getCbamCertificatePriceEurPerTco2e());

        AdvancedCertificatesResponse response = new AdvancedCertificatesResponse();
        response.setActualSpecificEmissionsTco2ePerTon(CalculationSupport.roundEmissions(request.getActualSpecificEmissionsTco2ePerTon()));
        response.setFreeAllowanceDeductionTco2ePerTon(CalculationSupport.roundEmissions(freeAllowanceDeduction));
        response.setThirdCountryCarbonPriceDeductionTco2ePerTon(CalculationSupport.roundEmissions(thirdCountryDeduction));
        response.setImportedQuantityTons(request.getImportedQuantityTons());
        response.setCertificatesToSurrender(CalculationSupport.roundEmissions(certificates));
        response.setCbamCertificatePriceEurPerTco2e(request.getCbamCertificatePriceEurPerTco2e());
        response.setEstimatedCostEur(CalculationSupport.roundMoney(cost));
        response.setFormula("certificates = max(0, (A - B - C) × D)");
        return response;
    }
}
