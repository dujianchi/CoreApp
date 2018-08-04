package cn.dujc.core.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.UUID;

/**
 * 基本的Fragment。最好Fragment都要继承于此类
 * Created by du on 2017/9/19.
 */
public abstract class BaseDialogFragment extends DialogFragment implements IBaseUI, IBaseUI.IPermissionKeeperCallback {

    private IStarter mStarter = null;
    private IParams mParams = null;
    private IPermissionKeeper mPermissionKeeper = null;

    private boolean mLoaded = false;//是否已经载入
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
                mRootView = inflater.inflate(vid, container, false);
            } else {
                mRootView = rootView;
            }
        }

        if (mRootView != null) {
            final ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }

            ViewGroup.LayoutParams layoutParams = mRootView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT);
                mRootView.setLayoutParams(layoutParams);
            }
        }

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!mLoaded && mRootView != null) {
            mLoaded = true;
            initBasic(savedInstanceState);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        dialogFullscreen();
    }

    /**
     * @deprecated dialog没有必要处理toolbar
     */
    @Override
    @Deprecated
    public final View createRootView(View view) {
        return view;
    }

    /**
     * @deprecated dialog没有必要处理toolbar
     */
    @Override
    @Nullable
    @Deprecated
    public final Toolbar initToolbar(ViewGroup parent) {
        return null;
    }

    /**
     * @deprecated dialog没有必要处理toolbar
     */
    @Override
    @Nullable
    @Deprecated
    public TitleCompat getTitleCompat() {
        return null;
    }

    @Override
    public IStarter starter() {
        if (mStarter == null) mStarter = new IStarterImpl(this);
        else mStarter.clear();//为什么要clear呢？想了想，实际上我用的一直是同一个starter，那么，如果一直界面往不同界面都传了值，它就会一直累加……
        return mStarter;
    }

    @Override
    public IParams extras() {
        if (mParams == null) mParams = new IParamsImpl(mActivity);
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

    /**
     * 关联主界面 **只有在使用自定义View时使用**
     */
    @Override
    @Nullable
    public View getViewV() {
        return null;
    }

    @Nullable
    public final View findViewById(int resId) {
        return mRootView != null ? mRootView.findViewById(resId) : null;
    }

    public void dialogFullscreen() {
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
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
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final Fragment preDialog = fragmentManager.findFragmentByTag(tag);
        if (preDialog != null) {
            fragmentManager.beginTransaction().remove(preDialog).commit();
        }
        show(fragmentManager, tag);
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
}
