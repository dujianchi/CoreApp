package cn.dujc.core.ui;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import cn.dujc.core.initializer.toolbar.IToolbar;

/**
 * toolbar组装工具
 */
class BaseToolbarHelper {

    /**
     * rootView和toolbar处理逻辑
     */
    static View[] createRootViewAndToolbar(@IToolbar.Style int toolbarStyle, Context context, IBaseUI.WithToolbar baseUI, View contentView) {
        //if (toolbarStyle == null) return noneRootView(baseUI, contentView);
        switch (toolbarStyle) {
            case IToolbar.LINEAR:
                return BaseToolbarHelper.linearRootView(context, baseUI, contentView);
            case IToolbar.FRAME:
                return BaseToolbarHelper.frameRootView(context, baseUI, contentView);
            case IToolbar.COORDINATOR:
                return BaseToolbarHelper.coordinatorRootView(context, baseUI, contentView);
            default:
            case IToolbar.NONE:
                return noneRootView(baseUI, contentView);
        }
    }

    private static View[] noneRootView(IBaseUI.WithToolbar baseUI, View contentView){
        final View[] rootAndTool = new View[2];
        if (contentView instanceof ViewGroup) {
            rootAndTool[1] = baseUI.initToolbar((ViewGroup) contentView);
        }
        rootAndTool[0] = contentView;
        return rootAndTool;
    }

    /**
     * 标题与界面线性排列
     */
    private static View[] linearRootView(Context context, IBaseUI.WithToolbar baseUI, View contentView) {
        LinearLayout layout = new LinearLayout(context);
        View toolbar = baseUI.initToolbar(layout);
        if (toolbar != null) {
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(toolbar);
            layout.addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            return new View[]{layout, toolbar};
        } else {
            layout = null;
            return new View[]{contentView, null};
        }
    }

    /**
     * 标题与界面帧层叠
     */
    private static View[] frameRootView(Context context, IBaseUI.WithToolbar baseUI, View contentView) {
        FrameLayout layout = new FrameLayout(context);
        View toolbar = baseUI.initToolbar(layout);
        if (toolbar != null) {
            layout.addView(contentView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            layout.addView(toolbar);
            return new View[]{layout, toolbar};
        } else {
            layout = null;
            return new View[]{contentView, null};
        }
    }

    /**
     * coordinator布局
     */
    private static View[] coordinatorRootView(Context context, IBaseUI.WithToolbar baseUI, View contentView) {
        CoordinatorLayout layout = new CoordinatorLayout(context);
        View toolbar = baseUI.initToolbar(layout);
        if (toolbar != null) {
            final CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
            params.setBehavior(new AppBarLayout.ScrollingViewBehavior());

            if (contentView instanceof RecyclerView || contentView instanceof NestedScrollView || contentView instanceof SwipeRefreshLayout || contentView instanceof ScrollView || contentView instanceof ListView || contentView instanceof GridView || contentView instanceof ViewPager) {
                layout.addView(contentView, params);
            } else {
                NestedScrollView nestedScrollView = new NestedScrollView(context);
                nestedScrollView.addView(contentView, new NestedScrollView.LayoutParams(NestedScrollView.LayoutParams.MATCH_PARENT, NestedScrollView.LayoutParams.MATCH_PARENT));
                layout.addView(nestedScrollView, params);
            }
            layout.addView(toolbar);
            return new View[]{layout, toolbar};
        } else {
            layout = null;
            return new View[]{contentView, null};
        }
    }
}
