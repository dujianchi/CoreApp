package cn.dujc.core.initializer.baselist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import cn.dujc.core.adapter.BaseQuickAdapter;

public interface IBaseListSetup {

    public void recyclerViewOtherSetup(Context context, RecyclerView recyclerView, BaseQuickAdapter adapter);

    public static class Impl implements IBaseListSetup {

        @Override
        public void recyclerViewOtherSetup(Context context, RecyclerView recyclerView, BaseQuickAdapter adapter) { }
    }
}
