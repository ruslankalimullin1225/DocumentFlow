package com.docflow.utils;

import java.math.BigDecimal;

public class ValidationUtils {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidNumber(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }

        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isPositiveNumber(String str) {
        if (!isValidNumber(str)) {
            return false;
        }

        try {
            BigDecimal value = new BigDecimal(str);
            return value.compareTo(BigDecimal.ZERO) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNonNegativeNumber(String str) {
        if (!isValidNumber(str)) {
            return false;
        }

        try {
            BigDecimal value = new BigDecimal(str);
            return value.compareTo(BigDecimal.ZERO) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static BigDecimal parseBigDecimal(String str, BigDecimal defaultValue) {
        if (!isValidNumber(str)) {
            return defaultValue;
        }

        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
