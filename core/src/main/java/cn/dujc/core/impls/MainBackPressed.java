package cn.dujc.core.impls;

import android.app.Activity;

import cn.dujc.core.util.ToastUtil;

public class MainBackPressed {

    private static final long[] LAST_BACK_PRESS = {0};

    public static void onBackPressed(Activity activity) {
        final long current = System.currentTimeMillis();
        if (current - LAST_BACK_PRESS[0] > 1500) {
            ToastUtil.showToast(activity, "再按一次退出");
            LAST_BACK_PRESS[0] = current;
        } else activity.onBackPressed();
    }
}
