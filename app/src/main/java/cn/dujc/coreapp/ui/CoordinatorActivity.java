package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date 2018/11/1 11:36 AM
 */
public class CoordinatorActivity extends BaseActivity {

    private final List<MultiTypeListActivity.Data> mList = new ArrayList<>();

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
    protected Style toolbarStyle() {
        return Style.COORDINATOR;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        final SwipeRefreshLayout srlLoader = findViewById(R.id.srl_loader);
        srlLoader.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlLoader.setRefreshing(false);
            }
        });
        final RecyclerView rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(mActivity));
        final MultiTypeListActivity.MultiTypeAdapter adapter = new MultiTypeListActivity.MultiTypeAdapter(mList);
        rvList.setAdapter(adapter);

        for (int index = 0; index < 100; index++) {
            final MultiTypeListActivity.Data data = new MultiTypeListActivity.Data();
            data.text = "text" + index;
            data.type = index % 7;
            mList.add(data);
        }
        adapter.setEnableLoadMore(false);
        adapter.notifyDataSetChanged();
    }
}
