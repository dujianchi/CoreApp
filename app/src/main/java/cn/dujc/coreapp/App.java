package cn.dujc.coreapp;

import android.app.Application;

import cn.dujc.core.toolbar.IToolbarHandler;

/**
 * @author du
 * date 2018/6/3 上午9:38
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        IToolbarHandler.setToolbarClass(this, ToolbarUtils.class);
    }
}
