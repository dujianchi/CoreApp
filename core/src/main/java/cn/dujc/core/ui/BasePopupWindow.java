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

    public void _onCreateView() {
        final int vid = getViewId();
        mRootView = getViewV();

        if (vid != 0 || mRootView != null) {
            if (vid != 0) {
                mRootView = new FrameLayout(mContext);
                LayoutInflater.from(mContext).inflate(vid, (ViewGroup) mRootView, true);
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

    @Nullable
    public final <T extends View> T findViewById(int resId) {
        return mRootView != null ? (T) mRootView.findViewById(resId) : null;
    }

    public int _getWidth() {
        return _getOutsideTouchable() ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
    }

    public int _getHeight() {
        return _getOutsideTouchable() ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
    }

    public boolean _getOutsideTouchable() {
        return true;
    }

    public int _getAnimationStyle() {
        return R.style.core_popup_animation;
    }

    public int _getBackgroundColor(Context context) {
        return Color.TRANSPARENT;
    }

    public Drawable _getBackgroundDrawable(Context context) {
        return new ColorDrawable(_getBackgroundColor(context));
    }

}
