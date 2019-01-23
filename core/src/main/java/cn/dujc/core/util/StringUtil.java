package cn.dujc.core.util;

import java.util.IllegalFormatException;

/**
 * 字符串相关工具
 * Created by du on 2017/10/16.
 */
public class StringUtil {

    /**
     * 字符串拼接
     *
     * @param objects
     * @return
     */
    public static String concat(Object... objects) {
        return concatWithSeparate(null, objects);
    }

    /**
     * 字符串拼接
     *
     * @param objects
     * @return
     */
    public static String concatWithSeparate(Object separate, Object... objects) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (objects != null) {
            for (Object obj : objects) {
                if (obj != null) {
                    stringBuilder.append(obj);
                    if (separate != null) {
                        stringBuilder.append(separate);
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 安全的格式化输出，如果参数对不上不会崩溃，而是输出format的内容
     */
    public static String format(String format, Object... args) {
        String result = format;
        try {
            result = String.format(format, args);
        } catch (IllegalFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result;
    }

}
