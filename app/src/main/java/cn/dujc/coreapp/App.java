package cn.dujc.coreapp;

import android.app.Application;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.squareup.leakcanary.LeakCanary;

import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.app.Core;
import cn.dujc.core.initializer.baselist.IBaseListSetup;
import cn.dujc.coreapp.ui.toolbar.ToolbarHandler;

/**
 * @author du
 * date 2018/10/31 1:54 PM
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Core.init(this, ToolbarHandler.class, ListSetupHelper.class);
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
    }

    public static class ListSetupHelper implements IBaseListSetup {
        @Override
        public void recyclerViewOtherSetup(Context context, RecyclerView recyclerView, BaseQuickAdapter adapter) {
            if (adapter != null) {
                adapter.setEmptyView(R.layout.layout_empty_view);
            }
        }
    }
}
