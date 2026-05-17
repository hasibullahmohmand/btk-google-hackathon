package com.carbonai.cbam.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Shared rounding helpers for emissions, money, and percentages.
 *
 * Why this class matters:
 * Financial and emissions calculations should be internally precise, but API
 * responses should still be readable and consistent. This helper centralizes
 * the rounding rules required by the project:
 * - emissions: 4 decimals
 * - money: 2 decimals
 * - percent: 2 decimals
 *
 * All rounding uses RoundingMode.HALF_UP.
 */
public final class CalculationSupport {

    static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private CalculationSupport() {
    }

    /**
     * Rounds a EUR amount to 2 decimals.
     *
     * Example:
     * 16302 -> 16302.00
     * 5187.004 -> 5187.00
     */
    public static BigDecimal roundMoney(BigDecimal value) {
        return value.setScale(2, ROUNDING_MODE);
    }

    /**
     * Rounds an emissions amount to 4 decimals.
     *
     * Example:
     * 214.5 -> 214.5000
     * 9.486432 -> 9.4864
     */
    public static BigDecimal roundEmissions(BigDecimal value) {
        return value.setScale(4, ROUNDING_MODE);
    }

    /**
     * Rounds a percentage to 2 decimals.
     *
     * Example:
     * 25.40567 -> 25.41
     */
    public static BigDecimal roundPercent(BigDecimal value) {
        return value.setScale(2, ROUNDING_MODE);
    }
}
