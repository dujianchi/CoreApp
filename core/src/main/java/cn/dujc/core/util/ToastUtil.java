package cn.dujc.core.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lucky on 2017/9/21.
 */
public class ToastUtil {
    ToastUtil() {
    }

    private static Toast mToast = null;

    public static void showToast(Context context, CharSequence text) {
        if (mToast != null) {//这么写是为了兼容下面那个方法，万一context为空的话不至于报错
            mToast.setText(text);
            mToast.show();
        } else if (context != null) {
            mToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public static void showToast(Context context, Object... objects) {
        showToast(context, StringUtil.concat(objects));
    }

    public static void showToastFormatted(Context context, String format, Object... args) {
        showToast(context, StringUtil.format(format, args));
    }

}
