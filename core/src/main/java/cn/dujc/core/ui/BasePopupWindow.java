package cn.dujc.core.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import cn.dujc.core.R;

public abstract class BasePopupWindow extends PopupWindow implements IBaseUI {

    protected Context mContext;
    protected View mRootView;
    private View mInsideView;

    public BasePopupWindow(Context context) {
        super(context);
        mContext = context;
        _onCreateView();
    }

    @Override
    public View getViewV() {
        return null;
    }

    public void showAtLocation(View parent) {
        showAtLocation(parent, Gravity.CENTER);
    }

    public void showAtLocation(View parent, int gravity) {
        showAtLocation(parent, gravity, 0, 0);
    }

    @Nullable
    public final <T extends View> T findViewById(int resId) {
        return mRootView != null ? (T) mRootView.findViewById(resId) : null;
    }

    public void _onCreateView() {
        final int vid = getViewId();
        mRootView = getViewV();

        if (vid != 0 || mRootView != null) {
            if (vid != 0) {
                mRootView = new FrameLayout(mContext);
                mInsideView = LayoutInflater.from(mContext).inflate(vid, (ViewGroup) mRootView, false);
                ((ViewGroup) mRootView).addView(mInsideView);
                mRootView.setOnTouchListener(new OnRootViewClick(this, mRootView, mInsideView));
            }
            setContentView(mRootView);
            setWidth(_getWidth());
            setHeight(_getHeight());
            setBackgroundDrawable(_getBackgroundDrawable(mContext));
            setAnimationStyle(_getAnimationStyle());
            setOutsideTouchable(_getOutsideTouchable());
            initBasic(null);
        }
    }

    public int _getWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    public int _getHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    public boolean _getOutsideTouchable() {
        return true;
    }

    public int _getAnimationStyle() {
        return R.style.core_popup_animation;
    }

    public int _getBackgroundColor(Context context) {
        return Color.argb(128, 0, 0, 0);
    }

    public Drawable _getBackgroundDrawable(Context context) {
        return new ColorDrawable(_getBackgroundColor(context));
    }

    private static final class OnRootViewClick implements View.OnTouchListener {
        private final PopupWindow mPopupWindow;
        private final View mRootView, mInsideView;
        private final Handler mHandler = new Handler();
        private final VRunnable mRunnable = new VRunnable() {
            @Override
            public void run() {
                if (mView != null && mTouched) {
                    mLongClicked = mView.performLongClick();
                }
            }
        };

        public OnRootViewClick(PopupWindow popupWindow, View rootView, View insideView) {
            mPopupWindow = popupWindow;
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
                    if (mPopupWindow != null) {
                        boolean touchable = mPopupWindow.isOutsideTouchable();
                        if (touchable) {
                            int[] xy = new int[2];
                            mInsideView.getLocationOnScreen(xy);
                            int width = mInsideView.getWidth();
                            int height = mInsideView.getHeight();
                            float x = event.getRawX();
                            float y = event.getRawY();
                            if (x < xy[0] || y < xy[1] || x > xy[0] + width || y > xy[1] + height) {
                                mPopupWindow.dismiss();
                            }
                        }
                    }
                    return v.performClick();
                }
            }
            return mRunnable.mTouched || v.performClick();
        }

        private static abstract class VRunnable implements Runnable {
            public View mView;
            public boolean mTouched = false, mLongClicked = false;
        }
    }
}
