package cn.dujc.coreapp;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import cn.dujc.core.app.Core;
import cn.dujc.coreapp.ui.toolbar.ToolbarHandler;

/**
 * @author du
 * date 2018/10/31 1:54 PM
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Core.init(this, ToolbarHandler.class);
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
    }
}
