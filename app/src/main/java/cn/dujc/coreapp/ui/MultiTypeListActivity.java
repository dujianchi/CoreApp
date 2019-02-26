package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.adapter.util.TypeAsIdDelegate;
import cn.dujc.core.ui.BaseListActivity;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date 2018/10/31 8:47 PM
 */
public class MultiTypeListActivity extends BaseListActivity {

    private final List<Data> mList = new ArrayList<>();
    private final Random mRandom = new Random();
    private boolean mShowEmpty = true;

    @Nullable
    @Override
    public BaseQuickAdapter initAdapter() {
        final MultiTypeAdapter adapter = new MultiTypeAdapter(mList);
        /*adapter.setEmptyView(R.layout.layout_empty_view);
        adapter.addHeaderView(getLayoutInflater().inflate(R.layout.layout_empty_view, null));
        adapter.addFooterView(getLayoutInflater().inflate(R.layout.layout_empty_view, null));*/
        return adapter;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        super.initBasic(savedInstanceState);
        final BaseQuickAdapter adapter = getAdapter();
        //adapter.setEmptyView(R.layout.layout_empty_view);
        adapter.addHeaderView(getLayoutInflater().inflate(R.layout.layout_empty_view, null));
        adapter.addFooterView(getLayoutInflater().inflate(R.layout.layout_empty_view, null));
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void loadMore() {
        generateData(false);
    }

    @Override
    public void reload() {
        generateData(true);
    }

    private void generateData(boolean clean) {
        if (clean) mList.clear();
        if (mShowEmpty = !mShowEmpty) {
            for (int index = 0; index < 10; index++) {
                final Data data = new Data();
                data.text = "text" + index;
                data.type = mRandom.nextInt(3);
                mList.add(data);
            }
        }
        notifyDataSetChanged(mList.size() >= 230, false);
    }

    public static class Data {
        String text;
        int type = 0;
    }

    public static class MultiTypeAdapter extends BaseAdapter<Data> {

        public MultiTypeAdapter(@Nullable List<Data> data) {
            super(data);
            /*final MultiTypeDelegate<Data> delegate = new MultiTypeDelegate<Data>() {
                @Override
                protected int getItemType(Data data) {
                    return data.type == 0 ? 0 : 1;
                }
            };
            delegate.registerItemType(0, R.layout.item_multi_0)
                    .registerItemType(1, R.layout.item_multi_1);
            setMultiTypeDelegate(delegate);*/
            /*IMultiTypeDelegate<Data> delegate = new TypeAsIdDelegate<Data>() {
                @Override
                protected int getItemLayoutId(Data data) {
                    return data.type == 0 ? R.layout.item_multi_0 : R.layout.item_multi_1;
                }
            };
            setMultiTypeDelegate(delegate);*/
            new TypeAsIdDelegate<Data>() {
                @Override
                protected int getItemLayoutId(Data data) {
                    return data.type == 0 ? R.layout.item_multi_0 : R.layout.item_multi_1;
                }
            }.setup(this);
        }

        @Override
        protected void convert(BaseViewHolder helper, Data item) {
            helper.setText(R.id.text1, item.text);
        }
    }
}
