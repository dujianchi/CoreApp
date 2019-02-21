package cn.dujc.coreapp.ui;

import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.adapter.entity.IExpandable;
import cn.dujc.core.adapter.util.MultiTypeDelegate;
import cn.dujc.core.ui.BaseListActivity;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date 2018/10/31 8:47 PM
 */
public class ExpendListActivity extends BaseListActivity {

    private final List<Object> mList = new ArrayList<>();
    private final Random mRandom = new Random();

    @Nullable
    @Override
    public BaseQuickAdapter initAdapter() {
        return new ExpandAdapter(mList);
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
        for (int index = 0; index < 10; index++) {
            final DataOutter data = new DataOutter();
            final int innerSize = mRandom.nextInt(5);
            for (int innerIndex = 0; innerIndex < innerSize; innerIndex++) {
                data.list.add(new DataInner());
            }
            data.text = "text" + index + "  size is " + innerSize;
            mList.add(data);
        }
        notifyDataSetChanged(false, false);
        getAdapter().expandAll();
    }

    public static class DataOutter implements IExpandable<DataInner> {
        String text;
        List<DataInner> list = new ArrayList<>();
        private boolean mExpanded = false;

        @Override
        public boolean isExpanded() {
            return mExpanded;
        }

        @Override
        public void setExpanded(boolean expanded) {
            mExpanded = expanded;
        }

        @Override
        public List<DataInner> getSubItems() {
            return list;
        }

        @Override
        public int getLevel() {
            return 0;
        }
    }

    public static class DataInner {
        public String text = "inner not matter";
    }

    public static class ExpandAdapter extends BaseAdapter<Object> {

        public ExpandAdapter(@Nullable List<Object> data) {
            super(data);
            final MultiTypeDelegate<Object> delegate = new MultiTypeDelegate<Object>() {
                @Override
                protected int getItemType(Object data) {
                    return data instanceof DataOutter ? 1 : 0;
                }
            };
            delegate.registerItemType(1, R.layout.item_expand_0)
                    .registerItemType(0, R.layout.item_expand_1);
            setMultiTypeDelegate(delegate);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            if (item instanceof DataOutter) {
                helper.setText(R.id.text1, ((DataOutter) item).text);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((DataOutter) item).isExpanded()) {
                            collapse(helper.getAdapterPosition());
                        } else {
                            expand(helper.getAdapterPosition());
                        }
                    }
                });
            } else if (item instanceof DataInner) {
                helper.setText(R.id.text1, ((DataInner) item).text);
            }
        }
    }
}
