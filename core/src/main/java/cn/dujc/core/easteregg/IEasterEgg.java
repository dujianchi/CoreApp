package cn.dujc.core.easteregg;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface IEasterEgg {

    public static final long LONG_LIMIT_MILLS = 3000L//长按触发极限，单位毫秒
            , PRESS_LIMIT_MILLS = 5000L;//按住触发极限，单位毫秒
    public static final int SLIDE_LIMIT_DP = 15;
    public static final byte TAP = 0b1, PRESS = 0b10, SLIDE = 0b100, LONG = 0b1000;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TAP//点击
            , PRESS//按住；超过3秒算长按，超过5秒算按住
            , SLIDE//滑动
            , LONG//长按；超过3秒算长按，超过5秒算按住
    })
    @interface Trigger {
    }

    public boolean canOpen();

    public void open();

    @Trigger
    public int[] trigger();
}
