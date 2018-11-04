package cn.dujc.core.toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import cn.dujc.core.R;
import cn.dujc.core.ui.IBaseUI;
import cn.dujc.core.ui.TitleCompat;
import cn.dujc.core.util.ContextUtil;

/**
 * @author du
 * date 2018/5/12 下午7:07
 */
public final class IToolbarHandler {

    private static final String CLASS = "CLASS", NAME = IToolbarHandler.class.getSimpleName();
    private static IToolbar sToolbar = null;

    private IToolbarHandler() { }

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static void setToolbarClass(Context context, Class<? extends IToolbar> toolbarClass) {
        if (context == null || toolbarClass == null) return;
        final String className = toolbarClass.getName();
        preferences(context).edit().putString(CLASS, className).apply();
        createToolbarByClass(toolbarClass);
    }

    private static void createToolbarByClass(Class<? extends IToolbar> clazz) {
        try {
            sToolbar = clazz.newInstance().create();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static IToolbar getToolbar(Context context) {
        if (sToolbar != null) return sToolbar;
        if (context == null) return null;
        try {
            final Class<?> toolbarClass = Class.forName(preferences(context).getString(CLASS, ""));
            createToolbarByClass((Class<? extends IToolbar>) toolbarClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return sToolbar;
    }

    public static View handleByContext(Object user, ViewGroup parent, Context context) {
        return handleByClassname(user, parent, getToolbar(context));
    }

    public static View handleByClassname(Object user, ViewGroup parent, IToolbar iToolbar) {
        if (iToolbar != null) {
            final List<Class<? extends IBaseUI>> include = iToolbar.include();
            boolean useHere = include == null || include.size() == 0;//为空即任何类都可以使用
            if (!useHere) {
                for (Class clazz : include) {//不为空则判断是否满足之类的类型
                    useHere = useHere || clazz.isInstance(user);
                }
            }
            if (useHere) {//如果符合了满足条件，那么判断是否被排除了
                final List<Class<? extends IBaseUI>> exclude = iToolbar.exclude();
                if (exclude != null && exclude.size() > 0) {
                    for (Class clazz : exclude) {
                        if (clazz.isInstance(user)) {
                            useHere = false;
                            break;
                        }
                    }
                }
            }
            if (useHere) {
                final View toolbar = iToolbar.normal(parent);
                if (toolbar != null) {
                    final View backView = toolbar.findViewById(R.id.toolbar_back_id);
                    final Activity activity = ContextUtil.getActivity(parent.getContext());
                    if (activity != null && backView != null) {
                        backView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                activity.onBackPressed();
                            }
                        });
                    }
                }
                return toolbar;
            }
        }
        return null;
    }

    public static void statusColor(Object user, Context context, TitleCompat titleCompat) {
        if (titleCompat != null && context != null) {
            final IToolbar toolbar = getToolbar(context);
            if (toolbar != null) {
                final List<Class<? extends IBaseUI>> include = toolbar.include();
                boolean useHere = include == null || include.size() == 0;//为空即任何类都可以使用
                if (!useHere) {
                    for (Class clazz : include) {//不为空则判断是否满足之类的类型
                        useHere = useHere || clazz.isInstance(user);
                    }
                }
                if (useHere) {//如果符合了满足条件，那么判断是否被排除了
                    final List<Class<? extends IBaseUI>> exclude = toolbar.exclude();
                    if (exclude != null && exclude.size() > 0) {
                        for (Class clazz : exclude) {
                            if (clazz.isInstance(user)) {
                                useHere = false;
                                break;
                            }
                        }
                    }
                }
                if (useHere) {
                    final int color = toolbar.statusBarColor(context);
                    if (color != 0) {
                        final IToolbar.StatusBarMode mode = toolbar.statusBarMode();
                        // mode 可能为null，switch可能不安全
                        if (mode == IToolbar.StatusBarMode.AUTO) {
                            final boolean darkColor = TitleCompat.FlymeStatusbarColorUtils.isBlackColor(color, 120);
                            //上面这个判断是判断颜色是否是深色，所以状态栏就跟颜色相反
                            titleCompat.setStatusBarMode(!darkColor);
                        } else if (mode == IToolbar.StatusBarMode.DARK) {
                            titleCompat.setStatusBarMode(true);
                        } else if (mode == IToolbar.StatusBarMode.LIGHT) {
                            titleCompat.setStatusBarMode(false);
                        }
                        titleCompat.setFakeStatusBarColor(color);
                    }
                }
            }
        }
    }

}
