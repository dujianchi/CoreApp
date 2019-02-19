package cn.dujc.core.ui.func;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class OnRootViewClick implements View.OnTouchListener {

    private final IDialog mDialog;
    private final View mRootView, mInsideView;
    private final Handler mHandler = new Handler();
    private final OnRootViewClick.VRunnable mRunnable = new OnRootViewClick.VRunnable() {
        @Override
        public void run() {
            if (mView != null && mTouched) {
                mLongClicked = mView.performLongClick();
            }
        }
    };

    public OnRootViewClick(IDialog dialog, View rootView, View insideView) {
        mDialog = dialog;
        mRootView = rootView;
        mInsideView = insideView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mRootView != null && mInsideView != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunnable.mTouched = true;
                mRunnable.mLongClicked = false;
                mHandler.removeCallbacks(mRunnable);
                mRunnable.mView = v;
                mHandler.postDelayed(mRunnable, 3000);
            } else if (mRunnable.mLongClicked) {
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mRunnable.mTouched = false;
                mRunnable.mLongClicked = false;
                mHandler.removeCallbacks(mRunnable);
                mRunnable.mView = null;
                if (mDialog != null) {
                    boolean dismissible = mDialog._dismissible();
                    if (dismissible) {
                        int[] xy = new int[2];
                        mInsideView.getLocationOnScreen(xy);
                        int width = mInsideView.getWidth();
                        int height = mInsideView.getHeight();
                        float x = event.getRawX();
                        float y = event.getRawY();
                        if (x < xy[0] || y < xy[1] || x > xy[0] + width || y > xy[1] + height) {
                            mDialog._dismiss();
                            return true;
                        }
                    }
                }
                return v.performClick();
            }
        }
        return mRunnable.mTouched || (event.getAction() == MotionEvent.ACTION_UP && v.performClick());
    }

    private static abstract class VRunnable implements Runnable {
        public View mView;
        public boolean mTouched = false, mLongClicked = false;
    }
}
