package cn.dujc.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

/**
 * @author du
 * date 2018/5/11 上午11:27
 */
public class ContextUtil {

    /**
     * Get activity instance from desired context.
     */
    public static Activity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) return (Activity) context;
        if (context instanceof ContextWrapper)
            return getActivity(((ContextWrapper) context).getBaseContext());
        return null;
    }
}
