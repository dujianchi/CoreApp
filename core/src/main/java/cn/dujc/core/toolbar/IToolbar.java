package cn.dujc.core.toolbar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.dujc.core.ui.IBaseUI;

public interface IToolbar {

    //IToolbar get();

    enum StatusBarMode {
        NONE//不操作
        , AUTO//自动
        , DARK//深色
        , LIGHT//浅色
    }

    // /**
    //  * 推荐实现IToolbar的用单例模式，然后单例的方法用这个注解
    //  */
    // @Retention(RetentionPolicy.RUNTIME)
    // @Target(ElementType.METHOD)
    // public static @interface Instance {}

    View normal(ViewGroup parent);

    int statusBarColor(Context context);

    StatusBarMode statusBarMode();

    IBaseUI.WithToolbar.Style toolbarStyle();

    List<Class<? extends IBaseUI>> exclude();

}
