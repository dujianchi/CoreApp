package cn.dujc.core.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关工具
 * Created by lucky on 2017/10/16.
 */
public class StringUtil {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^1(0|3|4|5|8|9|7)\\d{9}$");

    /**
     * 判断是否是手机号
     *
     * @param tel
     * @return
     */
    public static boolean isPhone(String tel) {
        if (TextUtils.isEmpty(tel)) {
            return false;
        }
        Matcher matcher = PHONE_PATTERN.matcher(tel);
        return matcher.matches();
    }

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

}
