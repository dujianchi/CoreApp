package cn.dujc.core.initializer.toolbar;

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import cn.dujc.core.ui.IBaseUI;

public interface IToolbar {

    public static final int NONE = 0;
    public static final int LINEAR = 1, FRAME = 2, COORDINATOR = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE//无
            , LINEAR//线性排列
            , FRAME//帧（层叠）排列
            , COORDINATOR//协调布局排列
    })
    @interface Style { }

    public final static int AUTO = 1//自动
            , DARK = 2//深色
            , LIGHT = 3;//浅色

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE//不操作
            , AUTO//自动
            , DARK//深色
            , LIGHT//浅色
    })
    @interface StatusBarMode { }

    // /**
    //  * 推荐实现IToolbar的用单例模式，然后单例的方法用这个注解
    //  */
    // @Retention(RetentionPolicy.RUNTIME)
    // @Target(ElementType.METHOD)
    // public static @interface Instance {}

    View normal(ViewGroup parent);

    int statusBarColor(Context context);

    @StatusBarMode
    int statusBarMode();

    @Style
    int toolbarStyle();

    List<Class<? extends IBaseUI>> exclude();

}
