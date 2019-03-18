package cn.dujc.coreapp.ui;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.adapter.entity.SectionEntity;
import cn.dujc.core.ui.BaseListActivity;

public class SectionActivity extends BaseListActivity {
    private List<Section> mList = new ArrayList<>();

    @Nullable
    @Override
    public BaseQuickAdapter initAdapter() {
        return new SectionAdapter(android.R.layout.simple_list_item_1, mList);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void loadMore() {
        loadData(mList.size());
    }

    @Override
    public void reload() {
        loadData(0);
    }

    private void loadData(int start) {
        if (start == 0) mList.clear();
        for (int index = 0; index < 10; index++) {
            Section section = new Section(new Entity("text" + index));
            section.header="adsf";

            mList.add(section);
        }
        notifyDataSetChanged(mList.size() >= 50, false);
    }

    public static class Entity {
        public String text;

        public Entity(String text) {
            this.text = text;
        }
    }

    public static class Section extends SectionEntity<Entity> {
        public Section(Entity entity) {
            super(entity);
        }
    }

    public static class SectionAdapter extends BaseAdapter<Section> {

        public SectionAdapter(int layoutResId, @Nullable List<Section> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Section item) {
            helper.setText(android.R.id.text1, item.t.text);
        }
    }
}
