package cn.dujc.core.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import cn.dujc.core.R;
import cn.dujc.core.ui.func.IDialog;
import cn.dujc.core.ui.func.OnRootViewClick;

public abstract class BasePopupWindow extends PopupWindow implements IBaseUI {

    protected Context mContext;
    protected View mRootView;

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
                final View insideView = LayoutInflater.from(mContext).inflate(vid, (ViewGroup) mRootView, false);
                ((ViewGroup) mRootView).addView(insideView);
                mRootView.setOnTouchListener(new OnRootViewClick(new IDialog.PopupWindowImpl(this), mRootView, insideView));
            }
            setContentView(mRootView);
            setWidth(_getWidth());
            setHeight(_getHeight());
            setBackgroundDrawable(_getBackgroundDrawable(mContext));
            setAnimationStyle(_getAnimationStyle());
            setOutsideTouchable(_getOutsideTouchable());
            setFocusable(_getFocusable());
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

    public boolean _getFocusable() {
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

}
