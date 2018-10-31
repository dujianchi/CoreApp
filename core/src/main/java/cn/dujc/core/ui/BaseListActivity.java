package cn.dujc.core.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.dujc.core.R;
import cn.dujc.core.adapter.BaseQuickAdapter;

/**
 * 基本列表activity
 * Created by du on 2017/9/27.
 */
public abstract class BaseListActivity extends BaseActivity {

    private SwipeRefreshLayout mSrlLoader;
    private RecyclerView mRvList;
    private BaseQuickAdapter mQuickAdapter;

    @Override
    public int getViewId() {
        return R.layout.base_list_layout;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mSrlLoader = (SwipeRefreshLayout) findViewById(R.id.srl_loader);
        mRvList = (RecyclerView) findViewById(R.id.rv_list);

        if (mSrlLoader != null) {
            //mSrlLoader.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
            mSrlLoader.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (mQuickAdapter != null) {
                        mQuickAdapter.resetLoadMore();
                    }
                    reload();
                }
            });
        }

        mQuickAdapter = initAdapter();
        final RecyclerView.LayoutManager layoutManager = initLayoutManager();

        mRvList.setLayoutManager(layoutManager);
        if (mQuickAdapter != null) {
            mRvList.setAdapter(mQuickAdapter);

            mQuickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    loadMore();
                }
            }, mRvList);

            mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    BaseListActivity.this.onItemClick(position);
                }
            });

            //initAdapter.disableLoadMoreIfNotFullPage();
            mQuickAdapter.openLoadAnimation();
        }
        recyclerViewOtherSetup();

        loadAtFirst();
    }

    @Override
    protected void onDestroy() {
        if (mQuickAdapter != null) mQuickAdapter.onRecycled();
        super.onDestroy();
    }

    /**
     * 加载第一次数据，默认同reload方法
     */
    protected void loadAtFirst() {
        reload();
    }

    /**
     * 加载失败（部分刷新）
     */
    protected final void loadFailure() {
        refreshDone();
        if (mQuickAdapter != null) {
            mQuickAdapter.loadMoreFail();
        }
    }

    /**
     * 刷新结束
     */
    protected final void refreshDone() {
        if (mSrlLoader != null) {
            mSrlLoader.setRefreshing(false);
        }
    }

    /**
     * 加载结束
     * @param dataDone 数据加载结束
     * @param endGone adapter的尾部是否隐藏
     */
    protected void loadDone(boolean dataDone, boolean endGone) {
        if (mQuickAdapter != null) {
            if (dataDone) {
                mQuickAdapter.loadMoreEnd(endGone);
            } else {
                mQuickAdapter.loadMoreComplete();
            }
        }
    }

    /**
     * 全部刷新
     */
    protected final void notifyDataSetChanged(boolean done) {
        notifyDataSetChanged(done, true);
    }

    /**
     * 全部刷新
     */
    protected final void notifyDataSetChanged(boolean dataDone, boolean endGone) {
        refreshDone();
        if (mQuickAdapter != null) {
            loadDone(dataDone, endGone);
            mQuickAdapter.notifyDataSetChanged();
        }
    }

    @Nullable
    protected final <T> T getItem(int position) {
        return mQuickAdapter != null ? (T) mQuickAdapter.getItem(position) : null;
    }

    /**
     * 关于recyclerView的一些其他设置
     */
    protected void recyclerViewOtherSetup() {
        if (mRvList != null) {
            mRvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }
    }

    /**
     * 默认的RecyclerView.LayoutManager是LinearLayoutManager
     *
     * @return LinearLayoutManager
     */
    @Nullable
    protected RecyclerView.LayoutManager initLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    @Nullable
    public BaseQuickAdapter getAdapter() {
        return mQuickAdapter;
    }

    @Nullable
    public RecyclerView getRecyclerView() {
        return mRvList;
    }

    @Nullable
    protected abstract BaseQuickAdapter initAdapter();

    @Nullable
    protected final SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSrlLoader;
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

    protected abstract void onItemClick(int position);

    protected abstract void loadMore();

    protected abstract void reload();
}
