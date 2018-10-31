package cn.dujc.coreapp.ui;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.adapter.util.MultiTypeDelegate;
import cn.dujc.core.ui.BaseListActivity;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date 2018/10/31 8:47 PM
 */
public class MultiTypeListActivity extends BaseListActivity {

    private final List<Data> mList = new ArrayList<>();
    private final Random mRandom = new Random();

    @Nullable
    @Override
    protected BaseQuickAdapter initAdapter() {
        return new MultiTypeAdapter(mList);
    }

    @Override
    protected void onItemClick(int position) {

    }

    @Override
    protected void loadMore() {
        generateData(false);
    }

    @Override
    protected void reload() {
        generateData(true);
    }

    private void generateData(boolean clean) {
        if (clean) mList.clear();
        for (int index = 0; index < 10; index++) {
            final Data data = new Data();
            data.text = "text" + index;
            data.type = mRandom.nextInt(3);
            mList.add(data);
        }
        notifyDataSetChanged(false, false);
    }

    public static class Data {
        String text;
        int type = 0;
    }

    public static class MultiTypeAdapter extends BaseAdapter<Data> {

        public MultiTypeAdapter(@Nullable List<Data> data) {
            super(data);
            final MultiTypeDelegate<Data> delegate = new MultiTypeDelegate<Data>() {
                @Override
                protected int getItemType(Data data) {
                    return data.type == 0 ? 0 : 1;
                }
            };
            delegate.registerItemType(0, R.layout.item_multi_0)
                    .registerItemType(1, R.layout.item_multi_1);
            setMultiTypeDelegate(delegate);
        }

        @Override
        protected void convert(BaseViewHolder helper, Data item) {
            helper.setText(R.id.text1, item.text);
        }
    }
}
