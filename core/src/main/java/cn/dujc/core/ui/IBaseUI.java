package cn.dujc.core.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基本的UI类方法，包括activity和fragment
 * Created by du on 2018/2/14.
 */
public interface IBaseUI {
    /**
     * 自增的request code，每个跳转都是forresult的跳转，那么，今后只要记住跳转方法{@link #go(Class)}或{@link #go(Class, Bundle)}返回的int值
     * ，即为本次跳转产生的request code，从此不再管理request code，且不会再重复，因为不管在什么界面跳转，每次跳转都用了不同的request code（当然，崩溃重启的情况例外）
     */
    int[] _INCREMENT_REQUEST_CODE = {1};

    Toolbar initToolbar(ViewGroup parent);

    TitleCompat initTransStatusBar();

    View createRootView(View contentView);

    int go(Class<? extends Activity> activity);

    int go(Class<? extends Activity> activity, Bundle args);

    View getViewV();

    int getViewId();

    void initBasic(Bundle savedInstanceState);
}
