package cn.dujc.core.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import cn.dujc.core.R;

/**
 * Android的顶部沉浸效果 工具
 * Created by du on 2017/9/19.
 */
public class TitleCompat {

    public static final int DEFAULT_TINT_COLOR = Color.TRANSPARENT;

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
     * @deprecated 用{@link StatusBarPlaceholder}替代加padding的方案
     */
    @Deprecated
    public static TitleCompat setStatusBar(Activity activity, boolean translucent, boolean fits) {
        return new TitleCompat().init(activity, translucent, fits)
                .setFakeStatusBarColor(DEFAULT_TINT_COLOR);
    }

    /**
     * 设置状态栏
     *
     * @param activity    activity
     * @param translucent 状态栏是否透明
     */
    public static TitleCompat setStatusBar(Activity activity, boolean translucent) {
        return new TitleCompat().init(activity, translucent)
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
     *
     * @deprecated 用{@link StatusBarPlaceholder}替代加padding的方案
     */
    @Deprecated
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
     * 设计为每个activity都有不同的title，即需要每个activity皆有不同的titleCompat
     * 为了防止使用单例的初始化，所以将构造方法设为私有方法。使每个activity都需要
     * 调用静态方法[TitleCompat.setStatusBar] 来实例化本类
     */
    private TitleCompat init(Activity activity, boolean translucent) {
        mActivity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarHeight = getStatusBarHeight(activity.getResources());
            setTranslucentStatus(translucent);
        }
        return this;
    }

    /**
     * 设置自己定义的statusBar是否可见
     * statusBar的颜色实现，是做了一层假的View覆盖在系统statusBar和decorView之间，实际上系统statusBar还是透明的
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
     * statusBar的颜色实现，是做了一层假的View覆盖在系统statusBar和decorView之间，实际上系统statusBar还是透明的
     *
     * @param color
     */
    public TitleCompat setFakeStatusBarColor(int color) {
        setFakeStatusBarColor(new ColorDrawable(color));
        return this;
    }

    /**
     * 设置自己定义的statusBar背景
     * statusBar的颜色实现，是做了一层假的View覆盖在系统statusBar和decorView之间，实际上系统statusBar还是透明的
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
     * @deprecated 用{@link StatusBarPlaceholder}替代加padding的方案
     */
    @Deprecated
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
            final View decorView = win.getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (on) {
                    decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    win.setStatusBarColor(DEFAULT_TINT_COLOR);
                } else {
                    decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    win.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    //setContentFits(false);
                }
            } else {
                if (on) {
                    win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                } else {
                    win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    //setContentFits(false);
                }
            }

            if (mFakeStatusBarView == null) {
                final View statusBarPlaceholder = mActivity.findViewById(R.id.core_toolbar_status_bar_placeholder);
                if (statusBarPlaceholder == null) {
                    ViewGroup decorViewGroup = (ViewGroup) decorView;
                    mFakeStatusBarView = new View(mActivity);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mStatusBarHeight);
                    params.gravity = Gravity.TOP;
                    mFakeStatusBarView.setLayoutParams(params);
                    mFakeStatusBarView.setVisibility(View.GONE);
                    decorViewGroup.addView(mFakeStatusBarView);
                } else {
                    mFakeStatusBarView = statusBarPlaceholder;
                }
                mFakeStatusBarView.setBackgroundColor(DEFAULT_TINT_COLOR);
            }
        }
        return this;
    }

    /**
     * @deprecated 并不是说这个被弃用了，而是我没有对这个进行测试
     */
    @Deprecated
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

    /**
     * 为控件设置marginTop为statusBar的高度
     *
     * @deprecated 用{@link StatusBarPlaceholder}替代加padding的方案
     */
    @Deprecated
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

    /**
     * 为控件设置paddingTop为statusBar的高度
     *
     * @deprecated 用{@link StatusBarPlaceholder}替代加padding的方案
     */
    @Deprecated
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

    /**
     * 状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param dark 字体是否是深色字体
     */
    public void setStatusBarMode(boolean dark) {
        FlymeStatusbarColorUtils.setStatusBarDarkIcon(mActivity, dark);
        MIUISetStatusBarLightMode(mActivity, dark);
        AndroidSetStatusBarLightMode(mActivity, dark);
    }

    /**
     * 原生状态栏深浅颜色API
     */
    private static boolean AndroidSetStatusBarLightMode(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final View decorView = activity.getWindow().getDecorView();
            if (dark) {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            return true;
        }
        return false;
    }

    /**
     * 需要MIUIV6以上
     *
     * @param activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true *
     */
    private static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) { }
        }
        return result;
    }

    /**
     * 从flyme开放平台搞来的工具类
     */
    public static class FlymeStatusbarColorUtils {
        private static Method mSetStatusBarColorIcon;
        private static Method mSetStatusBarDarkIcon;
        private static Field mStatusBarColorFiled;
        private static int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 0;

        static {
            try {
                mSetStatusBarColorIcon = Activity.class.getMethod("setStatusBarDarkIcon", int.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                mSetStatusBarDarkIcon = Activity.class.getMethod("setStatusBarDarkIcon", boolean.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                mStatusBarColorFiled = WindowManager.LayoutParams.class.getField("statusBarColor");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            try {
                Field field = View.class.getField("SYSTEM_UI_FLAG_LIGHT_STATUS_BAR");
                SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = field.getInt(null);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        /**
         * 判断颜色是否偏黑色
         *
         * @param color 颜色
         * @param level 级别
         * @return
         */
        public static boolean isBlackColor(int color, int level) {
            int grey = toGrey(color);
            return grey < level;
        }

        /**
         * 颜色转换成灰度值
         *
         * @param rgb 颜色
         * @return　灰度值
         */
        public static int toGrey(int rgb) {
            int blue = rgb & 0x000000FF;
            int green = (rgb & 0x0000FF00) >> 8;
            int red = (rgb & 0x00FF0000) >> 16;
            return (red * 38 + green * 75 + blue * 15) >> 7;
        }

        /**
         * 设置状态栏字体图标颜色
         *
         * @param activity 当前activity
         * @param color    颜色
         */
        public static void setStatusBarDarkIcon(Activity activity, int color) {
            if (mSetStatusBarColorIcon != null) {
                try {
                    mSetStatusBarColorIcon.invoke(activity, color);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                boolean whiteColor = isBlackColor(color, 50);
                if (mStatusBarColorFiled != null) {
                    setStatusBarDarkIcon(activity, whiteColor, whiteColor);
                    setStatusBarDarkIcon(activity.getWindow(), color);
                } else {
                    setStatusBarDarkIcon(activity, whiteColor);
                }
            }
        }

        /**
         * 设置状态栏字体图标颜色(只限全屏非activity情况)
         *
         * @param window 当前窗口
         * @param color  颜色
         */
        public static void setStatusBarDarkIcon(Window window, int color) {
            try {
                setStatusBarColor(window, color);
                if (Build.VERSION.SDK_INT > 22) {
                    setStatusBarDarkIcon(window.getDecorView(), true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 设置状态栏字体图标颜色
         *
         * @param activity 当前activity
         * @param dark     是否深色 true为深色 false 为白色
         */
        public static void setStatusBarDarkIcon(Activity activity, boolean dark) {
            setStatusBarDarkIcon(activity, dark, true);
        }

        private static boolean changeMeizuFlag(WindowManager.LayoutParams winParams, String flagName, boolean on) {
            try {
                Field f = winParams.getClass().getDeclaredField(flagName);
                f.setAccessible(true);
                int bits = f.getInt(winParams);
                Field f2 = winParams.getClass().getDeclaredField("meizuFlags");
                f2.setAccessible(true);
                int meizuFlags = f2.getInt(winParams);
                int oldFlags = meizuFlags;
                if (on) {
                    meizuFlags |= bits;
                } else {
                    meizuFlags &= ~bits;
                }
                if (oldFlags != meizuFlags) {
                    f2.setInt(winParams, meizuFlags);
                    return true;
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * 设置状态栏颜色
         *
         * @param view
         * @param dark
         */
        private static void setStatusBarDarkIcon(View view, boolean dark) {
            int oldVis = view.getSystemUiVisibility();
            int newVis = oldVis;
            if (dark) {
                newVis |= SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                newVis &= ~SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            if (newVis != oldVis) {
                view.setSystemUiVisibility(newVis);
            }
        }

        /**
         * 设置状态栏颜色
         *
         * @param window
         * @param color
         */
        private static void setStatusBarColor(Window window, int color) {
            WindowManager.LayoutParams winParams = window.getAttributes();
            if (mStatusBarColorFiled != null) {
                try {
                    int oldColor = mStatusBarColorFiled.getInt(winParams);
                    if (oldColor != color) {
                        mStatusBarColorFiled.set(winParams, color);
                        window.setAttributes(winParams);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 设置状态栏字体图标颜色(只限全屏非activity情况)
         *
         * @param window 当前窗口
         * @param dark   是否深色 true为深色 false 为白色
         */
        public static void setStatusBarDarkIcon(Window window, boolean dark) {
            if (Build.VERSION.SDK_INT < 23) {
                changeMeizuFlag(window.getAttributes(), "MEIZU_FLAG_DARK_STATUS_BAR_ICON", dark);
            } else {
                View decorView = window.getDecorView();
                if (decorView != null) {
                    setStatusBarDarkIcon(decorView, dark);
                    setStatusBarColor(window, 0);
                }
            }
        }

        private static void setStatusBarDarkIcon(Activity activity, boolean dark, boolean flag) {
            if (mSetStatusBarDarkIcon != null) {
                try {
                    mSetStatusBarDarkIcon.invoke(activity, dark);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                if (flag) {
                    setStatusBarDarkIcon(activity.getWindow(), dark);
                }
            }
        }
    }
}
