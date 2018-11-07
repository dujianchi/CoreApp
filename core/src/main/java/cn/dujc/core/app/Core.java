package cn.dujc.core.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import cn.dujc.core.bridge.ActivityStackUtil;
import cn.dujc.core.toolbar.IToolbar;
import cn.dujc.core.toolbar.IToolbarHandler;

/**
 * 由于框架内有多个需要初始化的参数，所以采用这个来构造一个初始化的方法
 * ，同时可以重新实现Initializer或InitProvider来自定义初始化哪部分。
 * 当然一个个功能初始化也可以...
 */
public class Core {

    private static final Core CORE = new Core();

    private Core() {}

    /**
     * 初始化框架
     *
     * @param initializer 初始化器
     * @param provider    初始化提供者，提供初始化所需要的参数
     */
    public static void init(@NonNull Initializer initializer, @NonNull InitProvider provider) {
        final Application app = provider.app();
        initializer.initActivityStack(app);
        final Class<? extends IToolbar> iToolbar = provider.iToolbar();
        if (iToolbar != null) {
            initializer.initToolbarHelper(app, iToolbar);
        }
    }

    /**
     * 初始化框架
     */
    public static void init(Application app, Class<? extends IToolbar> IToolbar) {
        init(getInit(), createProvider(app, IToolbar));
    }

    public static Initializer getInit() {
        return Init.INIT;
    }

    public static InitProvider createProvider(Application app, Class<? extends IToolbar> IToolbar) {
        return new Provider(app, IToolbar);
    }

    public static class Init implements Initializer {

        static final Initializer INIT = new Init();

        @Override
        public void initActivityStack(Application app) {
            if (app != null) ActivityStackUtil.getInstance().initApp(app);
        }

        @Override
        public void initToolbarHelper(Context context, Class<? extends IToolbar> clazz) {
            if (context != null && clazz != null) IToolbarHandler.setToolbarClass(context, clazz);
        }
    }

    public static class Provider implements InitProvider {
        private final Application mApp;
        private final Class<? extends IToolbar> mIToolbar;

        public Provider(Application app, Class<? extends IToolbar> IToolbar) {
            mApp = app;
            mIToolbar = IToolbar;
        }

        @Override
        public Application app() {
            return mApp;
        }

        @Override
        public Class<? extends IToolbar> iToolbar() {
            return mIToolbar;
        }
    }
}
