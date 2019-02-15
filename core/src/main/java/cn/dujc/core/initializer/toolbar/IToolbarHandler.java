package cn.dujc.core.initializer.toolbar;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import cn.dujc.core.R;
import cn.dujc.core.app.Initializer;
import cn.dujc.core.ui.IBaseUI;
import cn.dujc.core.ui.TitleCompat;
import cn.dujc.core.util.ContextUtil;

/**
 * @author du
 * date 2018/5/12 下午7:07
 */
public final class IToolbarHandler {

    private static final String CLASS = "CORE_TOOLBAR_CLASS";
    private static IToolbar sToolbar = null;

    private IToolbarHandler() { }

    /**
     * 设置toolbar的处理类，仅推荐默认toolbar使用此方案，其他情况请在activity中重写initToolbar方法
     */
    public static void setToolbarClass(Context context, Class<? extends IToolbar> toolbarClass) {
        if (context == null || toolbarClass == null) return;
        final String className = toolbarClass.getName();
        Initializer.classesSavior(context).edit().putString(CLASS, className).apply();
        createToolbarByClass(toolbarClass);
    }

    /**
     * 从保存的类中获取toolbar实例。处理顺序为 1.静态公有方法  2.注解方法  3.创建实例
     */
    private static void createToolbarByClass(Class<? extends IToolbar> clazz) {
        try {
            final Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                final IToolbar fromStatic = createByStaticMethod(method);
                if (fromStatic != null) {
                    sToolbar = fromStatic;
                    return;
                }
            }
            //for (Method method : declaredMethods) {
            //    final IToolbar fromAnnotation = createByAnnotation(method);
            //    if (fromAnnotation != null) {
            //        sToolbar = fromAnnotation;
            //        return;
            //    }
            //}
            sToolbar = createByNewInstance(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从静态公有方法获取IToolbar实例
     */
    private static IToolbar createByStaticMethod(Method method) {
        try {
            final boolean isStatic = Modifier.isStatic(method.getModifiers());
            if (isStatic) {
                if (!method.isAccessible()) method.setAccessible(true);
                final Object invoke = method.invoke(null);
                if (invoke instanceof IToolbar) return (IToolbar) invoke;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ///**
    // * 从注解中获取IToolbar实例
    // */
    //public static IToolbar createByAnnotation(Method method) {
    //    try {
    //        final IToolbar.Instance instance = method.getAnnotation(IToolbar.Instance.class);
    //        if (instance != null) {
    //            if (!method.isAccessible()) method.setAccessible(true);
    //            return (IToolbar) method.invoke(null);
    //        }
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //    return null;
    //}

    /**
     * 用new的方式获取IToolbar实例。这是最终也是最暴力的方式，当静态类、注解都失败的时候
     * ，会执行这个方案，这个方案会尽可能地通过传递的类来强制创建一个IToolbar实例
     */
    public static IToolbar createByNewInstance(Class clazz) {
        final Constructor[] constructors = clazz.getDeclaredConstructors();
        AccessibleObject.setAccessible(constructors, true);
        for (Constructor constructor : constructors) {
            try {
                Object instance = constructor.newInstance();
                if (instance instanceof IToolbar) {
                    return (IToolbar) instance;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取IToolbar的实例
     */
    public static IToolbar getToolbar(Context context) {
        if (sToolbar != null) return sToolbar;
        if (context == null) return null;
        try {
            final Class<?> toolbarClass = Class.forName(Initializer.classesSavior(context).getString(CLASS, ""));
            createToolbarByClass((Class<? extends IToolbar>) toolbarClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return sToolbar;
    }

    /**
     * 生成toolbar的view并返回给IBaseUI使用
     *
     * @param user 当前需要使用toolbar的类，用于判断是否符合排除或包含条件，通常填this
     * @return toolbar的view
     */
    public static View generateToolbar(Object user, ViewGroup parent, Context context) {
        return generateToolbar(user, parent, getToolbar(context));
    }

    /**
     * 生成toolbar的view并返回给IBaseUI使用
     *
     * @param user 当前需要使用toolbar的类，用于判断是否符合排除或包含条件，通常填this
     * @return toolbar的view
     */
    public static View generateToolbar(Object user, ViewGroup parent, IToolbar iToolbar) {
        if (iToolbar != null) {
            //final List<Class<? extends IBaseUI>> include = iToolbar.include();
            //boolean useHere = include == null || include.size() == 0;//为空即任何类都可以使用
            //if (!useHere) {
            //    for (Class clazz : include) {//不为空则判断是否满足之类的类型
            //        useHere = useHere || clazz.isInstance(user);
            //    }
            //}
            //if (useHere) {//如果符合了满足条件，那么判断是否被排除了
            boolean useHere = true;
            final List<Class<? extends IBaseUI>> exclude = iToolbar.exclude();
            if (exclude != null && exclude.size() > 0) {
                for (Class clazz : exclude) {
                    if (clazz.isInstance(user)) {
                        useHere = false;
                        break;
                    }
                }
            }
            //}
            if (useHere) {
                final View toolbar = iToolbar.normal(parent);
                if (toolbar != null) {
                    final View backView = toolbar.findViewById(R.id.core_toolbar_back_id);
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

    /**
     * 设置statusBar的颜色和字体深浅，直接作用于titleCompat：为了兼容状态栏字体深浅颜色的设置
     *
     * @param user 当前需要使用toolbar的类，用于判断是否符合排除或包含条件，通常填this
     */
    public static void statusColor(Object user, Context context, TitleCompat titleCompat) {
        if (titleCompat != null && context != null) {
            final IToolbar toolbar = getToolbar(context);
            if (toolbar != null) {
                //final List<Class<? extends IBaseUI>> include = toolbar.include();
                //boolean useHere = include == null || include.size() == 0;//为空即任何类都可以使用
                //if (!useHere) {
                //    for (Class clazz : include) {//不为空则判断是否满足之类的类型
                //        useHere = useHere || clazz.isInstance(user);
                //    }
                //}
                //if (useHere) {//如果符合了满足条件，那么判断是否被排除了
                boolean useHere = true;
                final List<Class<? extends IBaseUI>> exclude = toolbar.exclude();
                if (exclude != null && exclude.size() > 0) {
                    for (Class clazz : exclude) {
                        if (clazz.isInstance(user)) {
                            useHere = false;
                            break;
                        }
                    }
                }
                //}
                if (useHere) {
                    final int color = toolbar.statusBarColor(context);
                    final int mode = toolbar.statusBarMode();
                    // mode 可能为null，switch可能不安全
                    if (mode == IToolbar.AUTO) {
                        final boolean darkColor = TitleCompat.FlymeStatusbarColorUtils.isBlackColor(color, 120);
                        //上面这个判断是判断颜色是否是深色，所以状态栏就跟颜色相反
                        titleCompat.setStatusBarMode(!darkColor);
                    } else if (mode == IToolbar.DARK) {
                        titleCompat.setStatusBarMode(true);
                    } else if (mode == IToolbar.LIGHT) {
                        titleCompat.setStatusBarMode(false);
                    }
                    titleCompat.setFakeStatusBarColor(color);
                }
            }
        }
    }

}
