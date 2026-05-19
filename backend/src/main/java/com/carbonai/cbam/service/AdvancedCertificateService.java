package com.carbonai.cbam.service;

import com.carbonai.cbam.dto.AdvancedCertificatesRequest;
import com.carbonai.cbam.dto.AdvancedCertificatesResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for certificate surrender calculations aligned to the raw markdown
 * source of truth.
 */
@Service
public class AdvancedCertificateService {

    /**
     * Calculates certificates to surrender using the sequence reflected in the
     * raw markdown sources:
     * 1. certificates correspond to embedded emissions;
     * 2. the obligation is adjusted for free allocation under the EU ETS;
     * 3. the number of certificates can be reduced for the carbon price
     *    effectively paid in the country of origin;
     * 4. the remaining certificates are costed using the ETS-linked certificate price.
     */
    public AdvancedCertificatesResponse calculateAdvancedCertificates(AdvancedCertificatesRequest request) {
        // Source traceability:
        // - pdfs/outputs/raw_markdown/OJ_L_202502620_EN_TXT.md provides the free allocation adjustment framework.
        // - pdfs/outputs/raw_markdown/CBAM Frequently Asked Questions_November 2023.md explains that
        //   effective carbon prices paid outside the EU reduce the CBAM obligation.
        // - pdfs/outputs/raw_markdown/OJ_L_202502548_EN_TXT.md provides the certificate pricing framework.
        // The formula sequence below is a project-level simplification, not a verbatim legal equation.
        BigDecimal totalEmbeddedEmissions = request.getActualSpecificEmbeddedEmissionsTco2ePerTon()
                .multiply(request.getImportedQuantityTons());
        BigDecimal specificEmbeddedEmissionsSubjectToSurrender = request.getActualSpecificEmbeddedEmissionsTco2ePerTon()
                .subtract(request.getSpecificEmbeddedFreeAllocationTco2ePerTon())
                .max(BigDecimal.ZERO);
        BigDecimal certificatesBeforeCarbonPriceAdjustment = specificEmbeddedEmissionsSubjectToSurrender
                .multiply(request.getImportedQuantityTons());
        BigDecimal carbonPriceReductionInCertificates = request.getEffectiveCarbonPricePaidInCountryOfOriginEurPerTco2e()
                .compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : certificatesBeforeCarbonPriceAdjustment
                .multiply(request.getEffectiveCarbonPricePaidInCountryOfOriginEurPerTco2e())
                .divide(request.getEuEtsWeeklyAveragePriceEurPerTco2e(), 8, CalculationSupport.ROUNDING_MODE);
        BigDecimal certificatesToSurrender = certificatesBeforeCarbonPriceAdjustment
                .subtract(carbonPriceReductionInCertificates)
                .max(BigDecimal.ZERO);
        BigDecimal cost = certificatesToSurrender.multiply(request.getEuEtsWeeklyAveragePriceEurPerTco2e());

        AdvancedCertificatesResponse response = new AdvancedCertificatesResponse();
        response.setActualSpecificEmbeddedEmissionsTco2ePerTon(CalculationSupport.roundEmissions(request.getActualSpecificEmbeddedEmissionsTco2ePerTon()));
        response.setSpecificEmbeddedFreeAllocationTco2ePerTon(CalculationSupport.roundEmissions(request.getSpecificEmbeddedFreeAllocationTco2ePerTon()));
        response.setImportedQuantityTons(request.getImportedQuantityTons());
        response.setTotalEmbeddedEmissionsTco2e(CalculationSupport.roundEmissions(totalEmbeddedEmissions));
        response.setCertificateSurrenderObligationBeforeCarbonPriceAdjustment(CalculationSupport.roundEmissions(certificatesBeforeCarbonPriceAdjustment));
        response.setCarbonPriceReductionInCertificates(CalculationSupport.roundEmissions(carbonPriceReductionInCertificates));
        response.setCertificatesToSurrender(CalculationSupport.roundEmissions(certificatesToSurrender));
        response.setEffectiveCarbonPricePaidInCountryOfOriginEurPerTco2e(request.getEffectiveCarbonPricePaidInCountryOfOriginEurPerTco2e());
        response.setEuEtsWeeklyAveragePriceEurPerTco2e(request.getEuEtsWeeklyAveragePriceEurPerTco2e());
        response.setEstimatedCostEur(CalculationSupport.roundMoney(cost));
        response.setFormula(
                "certificatesBeforeCarbonPriceAdjustment = max(0, (specificEmbeddedEmissions - specificEmbeddedFreeAllocation) x importedQuantity); "
                        + "carbonPriceReductionInCertificates = certificatesBeforeCarbonPriceAdjustment x effectiveCarbonPricePaidInCountryOfOrigin / euEtsWeeklyAveragePrice; "
                        + "certificatesToSurrender = max(0, certificatesBeforeCarbonPriceAdjustment - carbonPriceReductionInCertificates)"
        );
        return response;
    }
}
