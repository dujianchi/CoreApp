package cn.dujc.core.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import cn.dujc.core.R;

public abstract class BaseDialog extends Dialog implements IBaseUI {

    protected Context mContext;
    protected View mRootView;
    private boolean mCancelable_ = true;
    private View mInsideView;

    public BaseDialog(Context context) {
        this(context, R.style.core_base_dialog_theme);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _onCreateView();
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        mCancelable_ = flag;
    }

    @Override
    public View getViewV() {
        return null;
    }

    @Nullable
    public final <T extends View> T findViewById(int resId) {
        return mRootView != null ? (T) mRootView.findViewById(resId) : null;
    }

    public boolean isCancelable() {
        return mCancelable_;
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
            Window window = getWindow();
            //window.setBackgroundDrawable(_getBackgroundDrawable(mContext));
            window.setLayout(_getWidth(), _getHeight());
            initBasic(null);
        }
    }

    public int _getWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    public int _getHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    /*public int _getBackgroundColor(Context context) {
        return Color.argb(128, 0, 0, 0);
    }*/

    /*public Drawable _getBackgroundDrawable(Context context) {
        return new ColorDrawable(_getBackgroundColor(context));
    }*/

    private static final class OnRootViewClick implements View.OnTouchListener {
        private final BaseDialog mDialog;
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

        public OnRootViewClick(BaseDialog dialog, View rootView, View insideView) {
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
                        boolean touchable = mDialog.isCancelable();
                        if (touchable) {
                            int[] xy = new int[2];
                            mInsideView.getLocationOnScreen(xy);
                            int width = mInsideView.getWidth();
                            int height = mInsideView.getHeight();
                            float x = event.getRawX();
                            float y = event.getRawY();
                            if (x < xy[0] || y < xy[1] || x > xy[0] + width || y > xy[1] + height) {
                                mDialog.dismiss();
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
}
