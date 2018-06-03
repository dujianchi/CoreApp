package cn.dujc.core.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

/**
 * 可刷新的activity
 * Created by du on 2017/9/27.
 */
public abstract class BaseRefreshableActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSrlLoader;

    @Override
    public View createRootView(View contentView) {
        return createRefreshRootView(super.createRootView(contentView));
    }

    private View createRefreshRootView(View rootView) {
        if (mSrlLoader == null) {
            mSrlLoader = new SwipeRefreshLayout(mActivity);
            mSrlLoader.setOnRefreshListener(this);
        }
        final View childAtTwo = mSrlLoader.getChildAt(1);
        if (childAtTwo != null) mSrlLoader.removeView(childAtTwo);
        mSrlLoader.addView(rootView);
        return mSrlLoader;
    }

    protected final SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSrlLoader;
    }

    protected final void refreshDone() {
        if (mSrlLoader != null) {
            mSrlLoader.setRefreshing(false);
        }
    }

    protected final void showRefreshing() {
        if (mSrlLoader != null) {
            mSrlLoader.setRefreshing(true);
        }
    }

    protected final void refreshEnable(boolean enable) {
        if (mSrlLoader != null) {
            mSrlLoader.setEnabled(enable);
        }
    }

}
