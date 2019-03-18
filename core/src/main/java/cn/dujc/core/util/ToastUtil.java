package cn.dujc.core.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * 在目前的实现方式中实现自定义view的toast会有问题，以后再来改（2018年11月7日）
 * Created by du on 2017/9/21.
 */
public class ToastUtil {
    public static final int DEFAULT_GRAVITY = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    public static final int DEFAULT_DURATION = Toast.LENGTH_SHORT;
    private static WeakReference<Toast> LAST_TOAST = null;

    ToastUtil() { }

    public static int getDefaultYOffset(Context context) {
        if (context == null) return 48;
        return (int) (context.getApplicationContext().getResources().getDisplayMetrics().density * 24 + 0.5);
    }

    @Nullable
    public static Toast current(Context context, CharSequence text) {
        if (context == null) return null;
        if (LAST_TOAST != null) {
            final Toast lastOne = LAST_TOAST.get();
            if (lastOne != null) lastOne.cancel();
        }
        @SuppressLint("ShowToast") Toast toast = Toast.makeText(context.getApplicationContext(), text, DEFAULT_DURATION);
        LAST_TOAST = new WeakReference<>(toast);
        return toast;
    }

    public static void showToast(Context context, CharSequence text) {
        showToast(context, text, DEFAULT_GRAVITY, getDefaultYOffset(context), DEFAULT_DURATION);
    }

    public static void showToastObjectsDefault(Context context, Object... objects) {
        showToastObjects(context, DEFAULT_GRAVITY, getDefaultYOffset(context), objects);
    }

    public static void showToastObjects(Context context, int gravity, int yOffset, Object... objects) {
        showToast(context, StringUtil.concat(objects), gravity, yOffset, DEFAULT_DURATION);
    }

    public static void showToastFormattedDefault(Context context, String format, Object... args) {
        showToastFormatted(context, DEFAULT_GRAVITY, getDefaultYOffset(context), DEFAULT_DURATION, format, args);
    }

    public static void showToastFormatted(Context context, int gravity, int yOffset, int duration, String format, Object... args) {
        showToast(context, StringUtil.format(format, args), gravity, yOffset, duration);
    }

    public static void showToast(Context context, CharSequence text, int gravity, int yOffset, int duration) {
        Toast toast = current(context, text);
        if (toast != null) {
            toast.setGravity(gravity, 0, yOffset);
            toast.setDuration(duration);
            toast.show();
        }
    }

}
