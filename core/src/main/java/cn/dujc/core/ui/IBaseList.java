package cn.dujc.core.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.dujc.core.R;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.initializer.baselist.IBaseListHandler;

/**
 * @author du
 * date 2018/11/1 4:33 PM
 */
interface IBaseList {

    int getViewId();

    void initBasic(Bundle savedInstanceState);

    void onDestroy_();

    /**
     * 协调刷新和Appbar
     */
    void coordinateRefreshAndAppbar();

    /**
     * 加载第一次数据，默认同reload方法
     */
    void loadAtFirst();

    /**
     * 加载失败（部分刷新）
     */
    void loadFailure();

    /**
     * 刷新结束
     */
    void refreshDone();

    /**
     * 加载结束
     *
     * @param dataDone 数据加载结束
     * @param endGone  adapter的尾部是否隐藏
     */
    void loadDone(boolean dataDone, boolean endGone);

    /**
     * 全部刷新
     */
    void notifyDataSetChanged(boolean done);

    /**
     * 全部刷新
     */
    void notifyDataSetChanged(boolean dataDone, boolean endGone);

    @Nullable
    <T> T getItem(int position);

    /**
     * 关于recyclerView的一些其他设置
     */
    void recyclerViewOtherSetup();

    /**
     * 双击标题回到顶部
     */
    void doubleClickTitleToTop();

    /**
     * 默认的RecyclerView.LayoutManager是LinearLayoutManager
     *
     * @return LinearLayoutManager
     */
    @Nullable
    RecyclerView.LayoutManager initLayoutManager();

    @Nullable
    public BaseQuickAdapter getAdapter();

    @Nullable
    public RecyclerView getRecyclerView();

    @Nullable
    SwipeRefreshLayout getSwipeRefreshLayout();

    void showRefreshing();

    void refreshEnable(boolean enable);

    interface UI extends IBaseList {

        @Nullable
        BaseQuickAdapter initAdapter();

        void onItemClick(int position);

        void loadMore();

        void reload();
    }

    abstract class AbsImpl implements IBaseList {

        private final UI mUI;
        private SwipeRefreshLayout mSrlLoader;
        private RecyclerView mRvList;
        private BaseQuickAdapter mQuickAdapter;
        private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
        private AppBarLayout mAppbarLayout;
        private long mLastDoubleTap = 0;

        public AbsImpl(UI UI) {
            mUI = UI;
        }

        abstract View findViewById(int id);

        abstract Context context();

        @Override
        public int getViewId() {
            return R.layout.core_base_list_layout;
        }

        @Override
        public void initBasic(Bundle savedInstanceState) {
            mSrlLoader = (SwipeRefreshLayout) findViewById(R.id.core_srl_loader);
            mRvList = (RecyclerView) findViewById(R.id.core_rv_list);
            mUI.doubleClickTitleToTop();

            if (mSrlLoader != null) {
                //mSrlLoader.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
                mSrlLoader.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (mQuickAdapter != null) {
                            mQuickAdapter.resetLoadMore();
                        }
                        mUI.reload();
                    }
                });
            }

            mQuickAdapter = mUI.initAdapter();
            final RecyclerView.LayoutManager layoutManager = mUI.initLayoutManager();

            mRvList.setLayoutManager(layoutManager);
            if (mQuickAdapter != null) {
                mRvList.setAdapter(mQuickAdapter);

                mQuickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        mUI.loadMore();
                    }
                }, mRvList);

                mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        mUI.onItemClick(position);
                    }
                });

                //initAdapter.disableLoadMoreIfNotFullPage();
                mQuickAdapter.openLoadAnimation();
            }
            mUI.recyclerViewOtherSetup();

            mUI.loadAtFirst();
            mUI.coordinateRefreshAndAppbar();
        }

        @Override
        public void onDestroy_() {
            if (mQuickAdapter != null) mQuickAdapter.onRecycled();
            if (mAppbarLayout != null && mOnOffsetChangedListener != null) {
                mAppbarLayout.removeOnOffsetChangedListener(mOnOffsetChangedListener);
            }
        }

        @Override
        public void coordinateRefreshAndAppbar() {
            View appbarLayout = findViewById(R.id.core_toolbar_appbar_layout);
            if (mSrlLoader != null && appbarLayout instanceof AppBarLayout) {
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

        @Override
        public void loadAtFirst() {
            mUI.reload();
        }

        @Override
        public void loadFailure() {
            mUI.refreshDone();
            if (mQuickAdapter != null) {
                mQuickAdapter.loadMoreFail();
            }
        }

        @Override
        public void refreshDone() {
            if (mSrlLoader != null) {
                mSrlLoader.setRefreshing(false);
            }
        }

        @Override
        public void loadDone(boolean dataDone, boolean endGone) {
            if (mQuickAdapter != null) {
                if (dataDone) {
                    mQuickAdapter.loadMoreEnd(endGone);
                } else {
                    mQuickAdapter.loadMoreComplete();
                }
            }
        }

        @Override
        public void notifyDataSetChanged(boolean done) {
            mUI.notifyDataSetChanged(done, true);
        }

        @Override
        public void notifyDataSetChanged(boolean dataDone, boolean endGone) {
            mUI.refreshDone();
            if (mQuickAdapter != null) {
                mUI.loadDone(dataDone, endGone);
                mQuickAdapter.notifyDataSetChanged();
            }
        }

        @Nullable
        @Override
        public <T> T getItem(int position) {
            return mQuickAdapter != null ? (T) mQuickAdapter.getItem(position) : null;
        }

        @Override
        public void recyclerViewOtherSetup() {
            IBaseListHandler.setup(context(), mRvList, mQuickAdapter);
        }

        @Override
        public void doubleClickTitleToTop() {
            final View view = findViewById(R.id.core_toolbar_title_id);
            if (view != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final long current = System.currentTimeMillis();
                        if (mRvList != null && current - mLastDoubleTap < 500)
                            mRvList.smoothScrollToPosition(0);
                        mLastDoubleTap = current;
                    }
                });
            }
        }

        @Nullable
        @Override
        public RecyclerView.LayoutManager initLayoutManager() {
            return new LinearLayoutManager(context());
        }

        @Nullable
        @Override
        public BaseQuickAdapter getAdapter() {
            return mQuickAdapter;
        }

        @Nullable
        @Override
        public RecyclerView getRecyclerView() {
            return mRvList;
        }

        @Nullable
        @Override
        public SwipeRefreshLayout getSwipeRefreshLayout() {
            return mSrlLoader;
        }

        @Override
        public void showRefreshing() {
            if (mSrlLoader != null) {
                mSrlLoader.setRefreshing(true);
            }
        }

        @Override
        public void refreshEnable(boolean enable) {
            if (mSrlLoader != null) {
                mSrlLoader.setEnabled(enable);
            }
        }
    }

    class FragmentImpl extends AbsImpl {
        private BaseListFragment mFragment;

        FragmentImpl(BaseListFragment fragment) {
            super(fragment);
            mFragment = fragment;
        }

        @Override
        View findViewById(int id) {
            return mFragment.findViewById(id);
        }

        @Override
        Context context() {
            return mFragment.mActivity;
        }
    }

    class ActivityImpl extends AbsImpl {

        private final BaseListActivity mActivity;

        ActivityImpl(BaseListActivity activity) {
            super(activity);
            mActivity = activity;
        }

        @Override
        View findViewById(int id) {
            return mActivity.findViewById(id);
        }

        @Override
        Context context() {
            return mActivity;
        }

    }
}
