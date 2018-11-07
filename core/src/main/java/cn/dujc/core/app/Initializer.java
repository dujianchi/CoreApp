package cn.dujc.core.app;

import android.app.Application;
import android.content.Context;

import cn.dujc.core.toolbar.IToolbar;

/**
 * 核心基础类
 */
public interface Initializer {

    public void initActivityStack(Application app);

    public void initToolbarHelper(Context context, Class<? extends IToolbar> clazz);
}
