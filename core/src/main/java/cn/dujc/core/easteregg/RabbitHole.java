package cn.dujc.core.easteregg;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RabbitHole {

    public static void registerEasterEggInto(View view, IEasterEgg egg) {
        if (view != null && egg != null) {
            view.setOnTouchListener(new EasterEggListener(view, egg));
        }
    }

    public static class EasterEggListener implements View.OnTouchListener {

        @NonNull
        private final IEasterEgg mEasterEgg;
        private final Handler mHandler;
        private final Runnable mRunnable;
        private final List<Integer> mTriggers = new ArrayList<>();

        private final int mSlideLimitPx/* = IEasterEgg.SLIDE_LIMIT_DP << 1*/;
        private long mLastTriggerTime = 0;

        public EasterEggListener(@NonNull View view, @NonNull IEasterEgg easterEgg) {
            mSlideLimitPx = (int) (view.getResources().getDisplayMetrics().density * IEasterEgg.SLIDE_LIMIT_DP + 0.5F);

            mEasterEgg = easterEgg;
            mHandler = new Handler(Looper.getMainLooper());
            mRunnable = new Runnable() {
                @Override
                public void run() {

                }
            };
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                final long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - mLastTriggerTime > IEasterEgg.PRESS_LIMIT_MILLS << 1) {//如果点击时间大于2个长按极限，则认为重新开始
                    mTriggers.clear();
                    mHandler.removeCallbacks(mRunnable);
                }

                mLastTriggerTime = currentTimeMillis;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {

            }

            return false;
        }
    }
}
