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
 * 基本列表fragment
 * Created by lucky on 2017/9/27.
 */
public abstract class BaseListFragment extends BaseFragment {

    private SwipeRefreshLayout mSrlLoader;
    protected RecyclerView mRvList;
    private BaseQuickAdapter mQuickAdapter;

    @Override
    public int getViewId() {
        return R.layout.base_list_layout;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mSrlLoader = (SwipeRefreshLayout) findViewById(R.id.srl_loader);
        mRvList = (RecyclerView) findViewById(R.id.rv_list);

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
                    BaseListFragment.this.onItemClick(position);
                }
            });

            //initAdapter.disableLoadMoreIfNotFullPage();
            mQuickAdapter.openLoadAnimation();
        }
        recyclerViewOtherSetup();

        loadAtFirst();
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
        loadingDone();
        if (mQuickAdapter != null) {
            mQuickAdapter.loadMoreFail();
        }
    }

    /**
     * 刷新结束
     */
    protected final void loadingDone() {
        if (mSrlLoader != null) {
            mSrlLoader.setRefreshing(false);
        }
    }

    /**
     * 全部刷新
     */
    protected final void notifyDataSetChanged(boolean done) {
        loadingDone();
        if (mQuickAdapter != null) {
            if (done){
                mQuickAdapter.loadMoreEnd(true);
            }else{
                mQuickAdapter.loadMoreComplete();
            }
            mQuickAdapter.notifyDataSetChanged();
        }
    }

    protected final <T> T getItem(int position) {
        return mQuickAdapter != null ? (T) mQuickAdapter.getItem(position) : null;
    }

    /**
     * 关于recyclerView的一些其他设置
     */
    protected void recyclerViewOtherSetup() {
        if (mRvList != null) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            mRvList.addItemDecoration(dividerItemDecoration);
        }
    }

    /**
     * 默认的RecyclerView.LayoutManager是LinearLayoutManager
     * @return LinearLayoutManager
     */
    protected RecyclerView.LayoutManager initLayoutManager(){
        return new LinearLayoutManager(mActivity);
    }

    @Nullable protected abstract BaseQuickAdapter initAdapter();

    protected abstract void onItemClick(int position);

    protected abstract void loadMore();

    protected abstract void reload();
}
