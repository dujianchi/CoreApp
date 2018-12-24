package cn.dujc.core.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘助手，可以用来设置监听软键盘的弹出和落下、在软键盘外部点击收起软键盘等功能
 * Created by lucky on 2017/12/29.
 */
public class KeyboardAssistant implements View.OnLayoutChangeListener {

    public static interface OnKeyboardShowingChangeListener {
        public void onKeyboardShowing(int keyboardHeight);//显示中

        public void onKeyboardHiding();//隐藏中

        public void onKeyboardShowingGoHiding();//由显示到隐藏
    }

    public static class OnKeyboardShowingChangeListenerImpl
            implements OnKeyboardShowingChangeListener {//用class而不用interface的原因是因为我可以不用非得实现3个方法，而是想要哪个实现哪个

        public void onKeyboardShowing(int keyboardHeight) { }//显示中

        public void onKeyboardHiding() { }//隐藏中

        public void onKeyboardShowingGoHiding() { }//由显示到隐藏
    }

    public static boolean isSystemInputActive(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return manager != null && manager.isActive();
    }

    public static void showSystemInput(Context context) {
        if (KeyboardAssistant.isSystemInputActive(context)) {
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideSystemInput(Activity activity) {
        KeyboardAssistant.hideSystemInput(activity, activity.getWindow().getDecorView().getWindowToken());
    }

    public static void hideSystemInput(Context context, IBinder windowToken) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private final Activity mActivity;
    private final int mHeightPixels;
    private final OnKeyboardShowingChangeListener mOnKeyboardShowingChangeListener;
    private boolean mShowing = false;//键盘是否显示中
    private boolean mTouchable = false;//Activity外部是否能点击
    private View mRootView;
    private int mDistanceDividend = 10;//点中的区域触发距离为屏幕的十分之一

    public static KeyboardAssistant createDefault(@NonNull Activity activity) {
        return new KeyboardAssistant(activity, new OnKeyboardShowingChangeListenerImpl());
    }

    public KeyboardAssistant(@NonNull Activity activity, OnKeyboardShowingChangeListener onKeyboardShowingChangeListener) {
        mActivity = activity;
        mOnKeyboardShowingChangeListener = onKeyboardShowingChangeListener;
        mHeightPixels = mActivity.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 关联布局
     *
     * @param rootView 需要被监听界面变化的控件，通常是listview、recyclerView、scrollView等
     */
    public KeyboardAssistant connect(View rootView) {
        if (rootView != null) {
            mRootView = rootView;
            rootView.addOnLayoutChangeListener(this);
        }
        return this;
    }

    /**
     * 取消关联布局
     */
    public void disconnect() {
        if (mRootView != null) {
            mRootView.removeOnLayoutChangeListener(this);
        }
    }

    /**
     * 处理Activity的dispatchTouchEvent事件，这个需要放在Activity的dispatchTouchEvent方法里调用
     * 还需要到{@link #setOutsideTouchable(boolean)}启用
     *
     * @param ev
     */
    public void dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (mTouchable && mShowing && ev.getAction() == MotionEvent.ACTION_DOWN) {
            final View currentFocus = mActivity.getCurrentFocus();
            //如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
            if (currentFocus != null && currentFocus instanceof EditText) {
                final int[] location = new int[2];
                currentFocus.getLocationOnScreen(location);
                final int t = location[1], b = t + currentFocus.getHeight();
                float y = ev.getRawY();
                final int distance = mHeightPixels / mDistanceDividend;//触发距离为屏幕的十分之一
                //当点中的区域距离文本框的距离大于屏幕高度的十分之一，认定点中了文本框外部的空白区域
                if (y > t + distance || y < b - distance) {//仅考虑Y轴上的距离，因为X轴上的距离可能会有一些诸如发送一类的按钮，就省得再去判断了
                    KeyboardAssistant.hideSystemInput(mActivity);
                }
            }
        }
    }

    /**
     * 设置外部是否可以点击，需要配合{@link #dispatchTouchEvent(MotionEvent)}使用
     *
     * @param touchable true就代表在键盘显示过程中，点击了外部会隐藏键盘
     */
    public KeyboardAssistant setOutsideTouchable(boolean touchable) {
        mTouchable = touchable;
        return this;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        final Rect rect = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        final int keyboardHeightMaybe = mHeightPixels - rect.bottom;
        if (keyboardHeightMaybe > 0) {
            mShowing = true;
            if (mOnKeyboardShowingChangeListener != null) {
                mOnKeyboardShowingChangeListener.onKeyboardShowing(keyboardHeightMaybe);
            }
        } else {
            if (mShowing) {
                if (mOnKeyboardShowingChangeListener != null) {
                    mOnKeyboardShowingChangeListener.onKeyboardShowingGoHiding();
                }
            }
            mShowing = false;
            if (mOnKeyboardShowingChangeListener != null) {
                mOnKeyboardShowingChangeListener.onKeyboardHiding();
            }
        }
    }
}
