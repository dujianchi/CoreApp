package cn.dujc.core.app;

import android.app.Application;

import cn.dujc.core.toolbar.IToolbar;

public interface InitProvider {
    Application app();

    Class<? extends IToolbar> iToolbar();
}
