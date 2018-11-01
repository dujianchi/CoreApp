package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date 2018/11/1 11:36 AM
 */
public class CoordinatorActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_coordinator;
    }

    @Nullable
    @Override
    public ViewGroup initToolbar(ViewGroup parent) {
        return (ViewGroup) LayoutInflater.from(mActivity).inflate(R.layout.toolbar_coordinator, parent, false);
    }

    @Override
    protected STYLE toolbarStyle() {
        return STYLE.COORDINATOR;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        final View appbarLayout = findViewById(cn.dujc.core.R.id.toolbar_appbar_layout);
        final SwipeRefreshLayout mSrlLoader = findViewById(R.id.srl_loader);
        final AppBarLayout mAppbarLayout;
        final AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
        if (mSrlLoader != null
                && appbarLayout instanceof AppBarLayout) {
            mAppbarLayout = (AppBarLayout) appbarLayout;
            mOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    mSrlLoader.setEnabled(verticalOffset >= 0);
                }
            };
            mAppbarLayout.addOnOffsetChangedListener(mOnOffsetChangedListener);
        }
    }
}
