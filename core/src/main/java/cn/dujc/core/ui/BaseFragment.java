package cn.dujc.core.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

/**
 * 基本的Fragment。最好Fragment都要继承于此类
 * Created by du on 2017/9/19.
 */
public abstract class BaseFragment extends Fragment implements IBaseUI, IBaseUI.IPermissionKeeperCallback {

    private IStarter mStarter = null;
    private IParams mParams = null;
    private IPermissionKeeper mPermissionKeeper = null;

    private boolean mLoaded = false;//是否已经载入
    protected Toolbar mToolbar;
    protected View mRootView;
    private TitleCompat mTitleCompat = null;
    protected Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        final int vid = getViewId();
        final View rootView = getViewV();
        Context context = container != null ? container.getContext() : null;

        if (mRootView == null && (vid != 0 || rootView != null) && context != null) {
            if (rootView == null) {
                mRootView = createRootView(inflater.inflate(vid, container, false));
            } else {
                mRootView = createRootView(rootView);
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
    public View createRootView(View view) {
        return linearRootView(view);
    }

    /**
     * 标题与界面线性排列
     */
    protected final View linearRootView(View view) {
        LinearLayout layout = new LinearLayout(getActivity());
        mToolbar = initToolbar(layout);
        if (mToolbar != null) {
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(mToolbar);
            layout.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                    , LinearLayout.LayoutParams.MATCH_PARENT));
            return layout;
        }
        return view;
    }

    /**
     * 标题与界面帧层叠
     */
    protected final View frameRootView(View view) {
        FrameLayout layout = new FrameLayout(getActivity());
        mToolbar = initToolbar(layout);
        if (mToolbar != null) {
            layout.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                    , LinearLayout.LayoutParams.MATCH_PARENT));
            layout.addView(mToolbar);
            return layout;
        }
        return view;
    }

    @Override
    @Nullable
    public Toolbar initToolbar(ViewGroup parent) {
        return null;
    }

    @Override
    @Nullable
    public TitleCompat getTitleCompat() {
        if (getActivity() instanceof BaseActivity) {
            mTitleCompat = ((BaseActivity) getActivity()).getTitleCompat();
        }
        return mTitleCompat;
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

    /**
     * 通知fragment改变了，需要这个的功能，在子类重写，然后其他地方调用这个子类的这个方法，就可以改动这个方法
     */
    public void notifyFragmentChanged() {/*在这重写需要更新fragment的动作*/}

}
