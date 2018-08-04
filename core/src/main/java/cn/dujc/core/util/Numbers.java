package cn.dujc.core.util;

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
        int result = defaultValue;
        try {
            String number;
            if (numberStr.contains(",")) number = numberStr.replace(",", "");
            else number = numberStr;
            result = Integer.valueOf(number.trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result;
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
        double result = defaultValue;
        try {
            String number;
            if (numberStr.contains(",")) number = numberStr.replace(",", "");
            else number = numberStr;
            result = Double.valueOf(number.trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result;
    }

}
