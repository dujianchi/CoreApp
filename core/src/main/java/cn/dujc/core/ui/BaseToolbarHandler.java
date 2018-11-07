package cn.dujc.core.ui;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

class BaseToolbarHandler {
    /**
     * 标题与界面线性排列
     */
    static View[] linearRootView(Context context, IBaseUI.WithToolbar baseUI, View contentView) {
        final LinearLayout layout = new LinearLayout(context);
        View toolbar = baseUI.initToolbar(layout);
        if (toolbar != null) {
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(toolbar);
            layout.addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            return new View[]{layout, toolbar};
        } else {
            return new View[]{contentView, toolbar};
        }
    }

    /**
     * 标题与界面帧层叠
     */
    static View[] frameRootView(Context context, IBaseUI.WithToolbar baseUI, View contentView) {
        final FrameLayout layout = new FrameLayout(context);
        View toolbar = baseUI.initToolbar(layout);
        if (toolbar != null) {
            layout.addView(contentView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            layout.addView(toolbar);
            return new View[]{layout, toolbar};
        } else {
            return new View[]{contentView, toolbar};
        }
    }

    /**
     * coordinator布局
     */
    static View[] coordinatorRootView(Context context, IBaseUI.WithToolbar baseUI, View contentView) {
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
            return new View[]{contentView, toolbar};
        }
    }
}
