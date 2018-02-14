package cn.dujc.core.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.dujc.core.util.LogUtil;

/**
 * 基本的Fragment。最好Fragment都要继承于此类
 * Created by lucky on 2017/9/19.
 */
public abstract class BaseFragment extends Fragment implements IBaseUI {

    private boolean mLoaded = false;//是否已经载入
    protected Toolbar mToolbar;
    protected View mRootView;
    protected TitleCompat mTitleCompat = null;
    protected Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        final int vid = getViewId();
        final View rootView = getViewV();
        Context context = container != null ? container.getContext() : null;

        mTitleCompat = initTransStatusBar();
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
    public Toolbar initToolbar(ViewGroup parent) {
        return null;
    }

    @Override
    public TitleCompat initTransStatusBar() {
        TitleCompat titleCompat = TitleCompat.setStatusBar(mActivity, true, true);
        return titleCompat;
    }

    @Override
    public int go(Class<? extends Activity> activity) {
        return go(activity, null);
    }

    @Override
    public int go(Class<? extends Activity> activity, Bundle args) {
        Intent intent = new Intent(mActivity, activity);
        if (args != null) {
            intent.putExtras(args);
        }
        int requestCode = _INCREMENT_REQUEST_CODE[0]++;
        if (requestCode >= 0xffff) {
            requestCode = _INCREMENT_REQUEST_CODE[0] = 1;
        }
        LogUtil.d("------------ request code = " + requestCode);
        startActivityForResult(intent, requestCode);
        return requestCode;
    }

    /**
     * 关联主界面 **只有在使用自定义View时使用**
     */
    @Override
    public View getViewV() {
        return null;
    }

    public final View findViewById(int resId) {
        return mRootView != null ? mRootView.findViewById(resId) : null;
    }

    /**
     * 通知fragment改变了，需要这个的功能，在子类重写，然后其他地方调用这个子类的这个方法，就可以改动这个方法
     */
    public void notifyFragmentChanged() {/*在这重写需要更新fragment的动作*/}

}
