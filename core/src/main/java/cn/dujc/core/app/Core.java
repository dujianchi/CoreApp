package cn.dujc.core.app;

import android.app.Application;
import android.content.Context;

import cn.dujc.core.bridge.ActivityStackUtil;
import cn.dujc.core.initializer.baselist.IBaseListHandler;
import cn.dujc.core.initializer.baselist.IBaseListSetup;
import cn.dujc.core.initializer.toolbar.IToolbar;
import cn.dujc.core.initializer.toolbar.IToolbarHandler;

/**
 * 由于框架内有多个需要初始化的参数，所以采用这个来构造一个初始化的方法
 * ，同时可以重新实现Initializer或InitProvider来自定义初始化哪部分。
 * 当然一个个功能初始化也可以...
 */
public class Core {

    private Core() {
    }

    /**
     * 初始化框架
     */
    public static void init(Application app, Class<? extends IToolbar> iToolbar) {
        init(app, iToolbar, IBaseListSetup.Impl.class);
    }

    /**
     * 初始化框架
     */
    public static void init(Application app, Class<? extends IToolbar> iToolbar, Class<? extends IBaseListSetup> iListSetup) {
        initActivityStack(app);
        if (iToolbar != null) {
            initToolbarHelper(app, iToolbar);
        }
        if (iListSetup != null) {
            initListSetup(app, iListSetup);
        }
    }

    private static void initActivityStack(Application app) {
        if (app != null) ActivityStackUtil.getInstance().initApp(app);
    }

    private static void initToolbarHelper(Context context, Class<? extends IToolbar> clazz) {
        if (context != null && clazz != null) IToolbarHandler.setToolbarClass(context, clazz);
    }

    private static void initListSetup(Context context, Class<? extends IBaseListSetup> clazz) {
        if (context != null && clazz != null) IBaseListHandler.setSetupClass(context, clazz);
    }
}
