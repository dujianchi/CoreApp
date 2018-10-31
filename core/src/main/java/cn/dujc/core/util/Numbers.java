package cn.dujc.core.util;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * @author du
 * date 2018/7/29 下午9:26
 */
public class Numbers {

    private Numbers() { }

    /**
     * 字符串转int
     *
     * @return 默认值 0
     */
    public static int stringToInt(String numberStr) {
        return stringToInt(numberStr, 0);
    }

    /**
     * 字符串转int
     */
    public static int stringToInt(String numberStr, int defaultValue) {
        try {
            return numberFromString(numberStr, defaultValue * 1.0).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 字符串转long
     */
    public static long stringToLong(String numberStr) {
        return stringToLong(numberStr, 0);
    }

    /**
     * 字符串转long
     */
    public static long stringToLong(String numberStr, long defaultValue) {
        try {
            return numberFromString(numberStr, defaultValue * 1.0).longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 字符串转double
     *
     * @return 默认值 0
     */
    public static double stringToDouble(String numberStr) {
        return stringToDouble(numberStr, 0);
    }

    /**
     * 字符串转double
     */
    public static double stringToDouble(String numberStr, double defaultValue) {
        try {
            return numberFromString(numberStr, defaultValue).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 两个数相乘
     *
     * @param multiplicand 被乘数
     * @param multiplier   乘数
     */
    public static String stringMultiply(String multiplicand, String multiplier, int scale) {
        if (scale < 0) scale = 0;
        try {
            final BigDecimal multiplicandB = numberFromString(multiplicand, null);
            final BigDecimal multiplierB = numberFromString(multiplier, null);
            return multiplicandB.multiply(multiplierB).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 两个数相减
     *
     * @param minuend    被减数
     * @param subtrahend 减数
     */
    public static String stringSubtract(String minuend, String subtrahend, int scale) {
        if (scale < 0) scale = 0;
        try {
            final BigDecimal minuendB = numberFromString(minuend, null);
            final BigDecimal subtrahendB = numberFromString(subtrahend, null);
            return minuendB.subtract(subtrahendB).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 两个数相加
     *
     * @param augend 被加数
     * @param addend 加数
     */
    public static String stringAdd(String augend, String addend, int scale) {
        if (scale < 0) scale = 0;
        try {
            final BigDecimal augendB = numberFromString(augend, null);
            final BigDecimal addendB = numberFromString(addend, null);
            return augendB.add(addendB).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 两个数相除
     *
     * @param dividend 被除数
     * @param divisor  除数
     */
    public static String stringDivide(String dividend, String divisor, int scale) {
        if (scale < 0) scale = 0;
        try {
            final BigDecimal dividendB = numberFromString(dividend, null);
            final BigDecimal divisorB = numberFromString(divisor, 1.0);
            return dividendB.divide(divisorB).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 格式化double格式
     */
    public static String formatDouble(double price, int scale) {
        return new BigDecimal(price).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 讲字符串转为BieDecimal高精度浮点数
     */
    public static BigDecimal numberFromString(String valueStr, Double defaultIfError) {
        BigDecimal result = defaultIfError != null ? new BigDecimal(defaultIfError) : null;
        if (TextUtils.isEmpty(valueStr)) return result;
        try {
            String number = valueStr;
            if (number.contains(",")) number = number.replace(",", "");
            if (number.contains(" ")) number = number.replace(" ", "");
            result = new BigDecimal(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result;
    }
}
