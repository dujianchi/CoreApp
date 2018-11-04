package cn.dujc.core.toolbar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.dujc.core.ui.IBaseUI;

public interface IToolbar {
    enum StatusBarMode {
        NONE//不操作
        , AUTO//自动
        , DARK//深色
        , LIGHT//浅色
    }

    IToolbar create();

    View normal(ViewGroup parent);

    int statusBarColor(Context context);

    StatusBarMode statusBarMode();

    List<Class<? extends IBaseUI>> exclude();

    List<Class<? extends IBaseUI>> include();
}
