package cn.dujc.core.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * 基本的Fragment。最好Fragment都要继承于此类
 * Created by lucky on 2017/9/19.
 */
public abstract class BaseFragment extends Fragment {

    private boolean mLoaded = false;//是否已经载入
    protected Toolbar mToolbar;
    protected View mRootView;
    protected TitleCompat mTitleCompat = null;
    protected Activity mActivity;
    //private View mContentLayout = null;//内容的View，即不包含toolbar的布局

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        final int vid = getViewId();
        final View rootView = getRootView();
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

    protected View createRootView(View view) {
        return linearRootView(view);
    }

    /**
     * 标题与界面线性排列
     */
    protected final View linearRootView(View view){
        //mContentLayout = view;
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
    protected final View frameRootView(View view){
        //mContentLayout = view;
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

    public Toolbar initToolbar(ViewGroup parent) {
        return null;
    }

    protected TitleCompat initTransStatusBar() {
        TitleCompat titleCompat = TitleCompat.setStatusBar(mActivity, true, true);
        return titleCompat;
    }

    public final View findViewById(int resId) {
        return mRootView != null ? mRootView.findViewById(resId) : null;
    }

    ///**
    // * 设置内容（即不包含toolbar的布局）的布局，如果在修改了显示方式之后，需要调用此方法
    // */
    //protected final void setContentLayout(View contentLayout) {
    //    mContentLayout = contentLayout;
    //}

    ///**
    // * 设置内容的View是否可用，可用在进行一个接口请求过程中，把整个界面设置为不可用，来防止点击或操作
    // * 此方法与{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}和{@link #createRootView(View)}的实现有关，如果
    // * 没有使用默认的方法，需要另外再设置{@link #setContentLayout(View)}
    // *
    // * @param enable 是否可用
    // */
    //protected final void setContentLayoutEnable(boolean enable) {
    //    if (mContentLayout != null) mContentLayout.setEnabled(enable);
    //}

    /**
     * 通知fragment改变了，需要这个的功能，在子类重写，然后其他地方调用这个子类的这个方法，就可以改动这个方法
     */
    public void notifyFragmentChanged(){}

    /**
     * 关联主界面 **只有在使用自定义View时使用**
     */
    public View getRootView() {
        return null;
    }

    /**
     * 关联主界面
     */
    public abstract int getViewId();

    /**
     * 基础初始化
     */
    public abstract void initBasic(Bundle savedInstanceState);
}
