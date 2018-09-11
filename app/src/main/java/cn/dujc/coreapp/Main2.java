package cn.dujc.coreapp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.entity.IExpandable;
import cn.dujc.core.ui.BaseListActivity;

/**
 * @author du
 * date 2018/9/7 下午3:24
 */
public class Main2 extends BaseListActivity {

    private final List<IExpandable> mList = new ArrayList<>();

    @Override
    public void initBasic(Bundle savedInstanceState) {
        super.initBasic(savedInstanceState);
        //模拟数据
        for (int index = 0; index < 20; index++) {
            String title = "index - " + index;
            Main2Entity.InnerEntity[] child = new Main2Entity.InnerEntity[5];
            for (int inner = 0; inner < child.length; inner++) {
                child[inner] = new Main2Entity.InnerEntity("index - " + index + " inner * " + inner);
            }
            mList.add(new Main2Entity(title, child));
        }
        notifyDataSetChanged(true);
    }

    @Nullable
    @Override
    protected BaseQuickAdapter initAdapter() {
        return new Main2Adapter(mList);
    }

    @Override
    protected void onItemClick(int position) {

    }

    @Override
    protected void loadMore() {

    }

    @Override
    protected void reload() {

    }
}
