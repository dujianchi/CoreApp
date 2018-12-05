package cn.dujc.core.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.List;
import java.util.UUID;

import cn.dujc.core.R;

/**
 * 基本的Fragment。最好Fragment都要继承于此类
 * Created by du on 2017/9/19.
 */
public abstract class BaseDialogFragment extends DialogFragment implements IBaseUI, IBaseUI.IPermissionKeeperCallback {

    private static final int THEME = R.style.Theme_AppCompat_Light_Dialog;

    private IStarter mStarter = null;
    private IParams mParams = null;
    private IPermissionKeeper mPermissionKeeper = null;

    private boolean mLoaded = false;//是否已经载入
    private volatile boolean mIsShowing = false;//是否在显示中
    private boolean mCanceledOnTouchOutside = true;//是否在点击外部到时候隐藏
    protected View mRootView;
    protected Activity mActivity;
    private String mTagUUId = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        final int vid = getViewId();
        final View rootView = getViewV();

        if (mRootView == null && (vid != 0 || rootView != null)) {
            if (rootView == null) {
                final FrameLayout layout = new FrameLayout(mActivity);
                inflater.inflate(vid, layout, true);
                mRootView = layout;
            } else {
                mRootView = rootView;
            }
        }
        if (mRootView != null && mRootView.getParent() instanceof ViewGroup) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }

        return mRootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, THEME);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Dialog dialog = getDialog();
        final Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);//似乎每次显示都会调用到这个方法，且每次dialog不设置到话斗鱼上次不同
        if (window != null) {
            window.setBackgroundDrawable(dialogBackground());
            window.setLayout(dialogWidth(), dialogHeight());
            window.setGravity(dialogGravity());
        }
        if (!mLoaded && mRootView != null) {
            mLoaded = true;
            initBasic(savedInstanceState);
        }
    }

    @Override
    public IStarter starter() {
        if (mStarter == null) mStarter = new IStarterImpl(this);
        else mStarter.clear();//为什么要clear呢？想了想，实际上我用的一直是同一个starter，那么，如果一直界面往不同界面都传了值，它就会一直累加……
        return mStarter;
    }

    @Override
    public IParams extras() {
        if (mParams == null) mParams = new FragmentParamsImpl(this);
        return mParams;
    }

    @Override
    public IPermissionKeeper permissionKeeper() {
        if (mPermissionKeeper == null) mPermissionKeeper = new IPermissionKeeperImpl(this, this);
        return mPermissionKeeper;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPermissionKeeper != null) {
            mPermissionKeeper.handOnActivityResult(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionKeeper != null) {
            mPermissionKeeper.handOnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onGranted(int requestCode, List<String> permissions) { }

    @Override
    public void onDenied(int requestCode, List<String> permissions) { }

    @Override
    public void dismiss() {
        super.dismiss();
        mIsShowing = false;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mIsShowing = false;
    }

    /**
     * 关联主界面 **只有在使用自定义View时使用**
     */
    @Override
    @Nullable
    public View getViewV() {
        return null;
    }

    @Nullable
    public final <T extends View> T findViewById(int resId) {
        return mRootView != null ? (T) mRootView.findViewById(resId) : null;
    }

    /**
     * 是否在点击外部到时候隐藏
     */
    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        mCanceledOnTouchOutside = canceledOnTouchOutside;
    }

    /**
     * dialog宽度
     */
    public int dialogWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * dialog高度
     */
    public int dialogHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * dialog对齐方式
     */
    public int dialogGravity() {
        return Gravity.CENTER;
    }

    /**
     * dialog对话框
     */
    public Drawable dialogBackground() {
        return new ColorDrawable(Color.TRANSPARENT);
    }

    /**
     * 通知fragment改变了，需要这个的功能，在子类重写，然后其他地方调用这个子类的这个方法，就可以改动这个方法
     */
    public void notifyFragmentChanged() {/*在这重写需要更新fragment的动作*/}

    /**
     * 显示对话框，相同tag只显示一个
     *
     * @param activity
     * @param tag
     */
    public void showOnly(FragmentActivity activity, String tag) {
        if (!isShowing()) {
            final FragmentManager fragmentManager = activity.getSupportFragmentManager();
            final Fragment preDialog = fragmentManager.findFragmentByTag(tag);
            if (preDialog != null) {
                fragmentManager.beginTransaction().remove(preDialog).commit();
            }
            show(fragmentManager, tag);
            mIsShowing = true;
        }
    }

    /**
     * 显示对话框，自动生成tag
     *
     * @param activity
     */
    public void showOnly(FragmentActivity activity) {
        if (TextUtils.isEmpty(mTagUUId)) {
            mTagUUId = UUID.randomUUID().toString();
        }
        showOnly(activity, mTagUUId);
    }

    /**
     * 是否在显示中
     */
    public synchronized final boolean isShowing() {
        return mIsShowing;
    }
}
