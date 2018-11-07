package cn.dujc.core.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import cn.dujc.core.adapter.BaseQuickAdapter;

/**
 * 基本列表activity
 * Created by du on 2017/9/27.
 */
public abstract class BaseListActivity extends BaseActivity implements IBaseList.UI {

    private IBaseList mBaseList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (mBaseList == null) mBaseList = new ActivityImpl(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getViewId() {
        return mBaseList.getViewId();
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mBaseList.initBasic(savedInstanceState);
    }

    @Override
    public void onDestroy_() {
        mBaseList.onDestroy_();
    }

    @Override
    public void coordinateRefreshAndAppbar() {
        mBaseList.coordinateRefreshAndAppbar();
    }

    @Override
    public void loadAtFirst() {
        mBaseList.loadAtFirst();
    }

    @Override
    public void loadFailure() {
        mBaseList.loadFailure();
    }

    @Override
    public void refreshDone() {
        mBaseList.refreshDone();
    }

    @Override
    public void loadDone(boolean dataDone, boolean endGone) {
        mBaseList.loadDone(dataDone, endGone);
    }

    @Override
    public void notifyDataSetChanged(boolean done) {
        mBaseList.notifyDataSetChanged(done);
    }

    @Override
    public void notifyDataSetChanged(boolean dataDone, boolean endGone) {
        mBaseList.notifyDataSetChanged(dataDone, endGone);
    }

    @Nullable
    @Override
    public <T> T getItem(int position) {
        return mBaseList.getItem(position);
    }

    @Override
    public void recyclerViewOtherSetup() {
        mBaseList.recyclerViewOtherSetup();
    }

    @Nullable
    @Override
    public RecyclerView.LayoutManager initLayoutManager() {
        return mBaseList.initLayoutManager();
    }

    @Nullable
    @Override
    public BaseQuickAdapter getAdapter() {
        return mBaseList.getAdapter();
    }

    @Nullable
    @Override
    public RecyclerView getRecyclerView() {
        return mBaseList.getRecyclerView();
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mBaseList.getSwipeRefreshLayout();
    }

    @Override
    public void showRefreshing() {
        mBaseList.showRefreshing();
    }

    @Override
    public void refreshEnable(boolean enable) {
        mBaseList.refreshEnable(enable);
    }

}
