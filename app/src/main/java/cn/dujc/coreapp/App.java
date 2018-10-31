package cn.dujc.coreapp;

import android.app.Application;

import cn.dujc.core.toolbar.IToolbarHandler;
import cn.dujc.coreapp.ui.toolbar.ToolbarHandler;

/**
 * @author du
 * date 2018/10/31 1:54 PM
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        IToolbarHandler.setToolbarClass(this, ToolbarHandler.class);
    }
}
