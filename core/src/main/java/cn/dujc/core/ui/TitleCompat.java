package cn.dujc.core.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.Map;

/**
 * Android的顶部沉浸效果 工具
 * Created by lucky on 2017/9/19.
 */
public class TitleCompat {

    public static final int DEFAULT_TINT_COLOR = 0x99000000;

    public static int getStatusBarHeight(Resources res) {
        int result = 0;
        final int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 设置状态栏
     *
     * @param activity    activity
     * @param translucent 状态栏是否透明
     * @param fits        是否适应内容
     * @return
     */
    public static TitleCompat setStatusBar(Activity activity, boolean translucent, boolean fits) {
        return new TitleCompat().init(activity, translucent, fits)
                .setFakeStatusBarColor(DEFAULT_TINT_COLOR);
    }

    private int mStatusBarHeight = 0;
    private View mFakeStatusBarView;
    private Activity mActivity;
    private Map<View, Integer> mTopViews = new ArrayMap<View, Integer>();//存放padding或margin顶部的View，防止重复新增

    /**
     * 设计为每个activity都有不同的title，即需要每个activity皆有不同的titleCompat
     * 为了防止使用单例的初始化，所以将构造方法设为私有方法。使每个activity都需要
     * 调用静态方法[TitleCompat.setStatusBar] 来实例化本类
     */
    private TitleCompat init(Activity activity, boolean translucent, boolean fits) {
        mActivity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarHeight = getStatusBarHeight(activity.getResources());
            setTranslucentStatus(translucent);
            if (translucent) {
                setContentFits(fits);
            }
        }
        return this;
    }

    /**
     * 设置自己定义的statusBar是否可见
     * <br/>statusBar的颜色实现，是做了一层假的View覆盖在系统statusBar和decorView之间，实际上系统statusBar还是透明的<br/>
     *
     * @param visibility
     */
    public TitleCompat setFakeStatusBarVisibility(boolean visibility) {
        if (mFakeStatusBarView != null) {
            mFakeStatusBarView.setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    /**
     * 是否将状态栏置底(true)，否则(false)置顶
     */
    public TitleCompat setFakeStatusBarLayerBottom(boolean bottom) {
        if (mFakeStatusBarView != null) {
            ViewGroup parent = (ViewGroup) mFakeStatusBarView.getParent();
            if (parent != null) {
                parent.removeView(mFakeStatusBarView);
                if (bottom) {
                    parent.addView(mFakeStatusBarView, 0);
                } else {
                    parent.addView(mFakeStatusBarView);
                }
            }
        }
        return this;
    }

    /**
     * 设置自己定义的statusBar背景颜色
     *
     * @param colorId id
     */
    public TitleCompat setFakeStatusBarColorId(int colorId) {
        int color = ContextCompat.getColor(mActivity, colorId);
        return setFakeStatusBarColor(color);
    }

    /**
     * 设置自己定义的statusBar背景颜色
     * <br/>statusBar的颜色实现，是做了一层假的View覆盖在系统statusBar和decorView之间，实际上系统statusBar还是透明的<br/>
     *
     * @param color
     */
    public TitleCompat setFakeStatusBarColor(int color) {
        setFakeStatusBarColor(new ColorDrawable(color));
        return this;
    }

    /**
     * 设置自己定义的statusBar背景
     * <br/>statusBar的颜色实现，是做了一层假的View覆盖在系统statusBar和decorView之间，实际上系统statusBar还是透明的<br/>
     *
     * @param drawable
     */
    public TitleCompat setFakeStatusBarColor(Drawable drawable) {
        if (mFakeStatusBarView != null) {
            setFakeStatusBarVisibility(true);
            ViewCompat.setBackground(mFakeStatusBarView, drawable);
        }
        return this;
    }

    /**
     * 设置内部内容适应
     *
     * @param fits 要使内容在statusBar下则true
     */
    public TitleCompat setContentFits(boolean fits) {
        if (mActivity != null) {
            ViewGroup contentFrameLayout = (ViewGroup) mActivity.findViewById(Window.ID_ANDROID_CONTENT);
            if (contentFrameLayout != null) {
                View parentView = contentFrameLayout.getChildAt(0);
                if (parentView != null) {
                    parentView.setPadding(parentView.getPaddingLeft(), fits ? mStatusBarHeight : 0
                            , parentView.getPaddingRight(), parentView.getPaddingBottom());
                }
            }
        }
        return this;
    }

    /**
     * 设置状态栏透明
     *
     * @param on 透明则true
     */
    public TitleCompat setTranslucentStatus(boolean on) {
        if (mActivity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = mActivity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
                setContentFits(false);
            }
            win.setAttributes(winParams);

            ViewGroup decorViewGroup = (ViewGroup) win.getDecorView();
            mFakeStatusBarView = new View(mActivity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mStatusBarHeight);
            params.gravity = Gravity.TOP;
            mFakeStatusBarView.setLayoutParams(params);
            mFakeStatusBarView.setBackgroundColor(DEFAULT_TINT_COLOR);
            mFakeStatusBarView.setVisibility(View.GONE);
            decorViewGroup.addView(mFakeStatusBarView);
        }
        return this;
    }

    public TitleCompat setTranslucentNavigation(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Window win = mActivity.getWindow();
            final WindowManager.LayoutParams winParams = win.getAttributes();
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
                setContentFits(false);
            }
            win.setAttributes(winParams);
        }
        return this;
    }

    public TitleCompat addMarginTop(View view) {
        if (view != null) {
            Integer top = mTopViews.get(view);
            if (top == null || top == 0) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams mlp = ((ViewGroup.MarginLayoutParams) layoutParams);
                    mlp.topMargin = mlp.topMargin + mStatusBarHeight;
                    mTopViews.put(view, mStatusBarHeight);
                }
            }
        }
        return this;
    }

    public TitleCompat addPaddingTop(View view) {
        if (view != null) {
            Integer top = mTopViews.get(view);
            if (top == null || top == 0) {
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + mStatusBarHeight
                        , view.getPaddingRight(), view.getPaddingBottom());
                mTopViews.put(view, mStatusBarHeight);
            }
        }
        return this;
    }
}
