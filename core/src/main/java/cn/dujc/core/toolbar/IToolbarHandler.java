package cn.dujc.core.toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import cn.dujc.core.R;
import cn.dujc.core.ui.TitleCompat;
import cn.dujc.core.util.ContextUtil;

/**
 * @author du
 * date 2018/5/12 下午7:07
 */
public final class IToolbarHandler {

    private static final String CLASS = "CLASS", NAME = IToolbarHandler.class.getSimpleName();
    private static String sCacheClassName = null;

    private IToolbarHandler() { }

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static void setToolbarClass(Context context, Class toolbarClass) {
        if (context == null || toolbarClass == null) return;
        final String className = toolbarClass.getName();
        sCacheClassName = className;
        preferences(context).edit()
                .putString(CLASS, className)
                .apply();
    }

    public static String getToolbarClass(Context context) {
        if (!TextUtils.isEmpty(sCacheClassName)) return sCacheClassName;
        if (context == null) return "";
        return sCacheClassName = preferences(context).getString(CLASS, "");
    }

    public static ViewGroup handleByContext(Object user, ViewGroup parent, Context context) {
        return handleByClassname(user, parent, getToolbarClass(context));
    }

    public static ViewGroup handleByClassname(Object user, ViewGroup parent, String classname) {
        if (!TextUtils.isEmpty(classname)) {
            try {
                final Class<?> toolbarClass = Class.forName(classname);
                for (Method method : toolbarClass.getDeclaredMethods()) {
                    final IToolbar iToolbar = method.getAnnotation(IToolbar.class);
                    if (iToolbar != null) {
                        boolean useHere = iToolbar.include().length == 0;//为空即任何类都可以使用
                        if (!useHere) {
                            for (Class clazz : iToolbar.include()) {//不为空则判断是否满足之类的类型
                                useHere = useHere || clazz.isInstance(user);
                            }
                        }
                        if (useHere) {//如果符合了满足条件，那么判断是否被排除了
                            for (Class clazz : iToolbar.exclude()) {
                                if (clazz.isInstance(user)) {
                                    useHere = false;
                                    break;
                                }
                            }
                        }
                        if (useHere) {
                            if (!method.isAccessible()) method.setAccessible(true);
                            final Class<?>[] parameterTypes = method.getParameterTypes();
                            Object[] args = new Object[parameterTypes.length];
                            for (int index = 0, length = parameterTypes.length; index < length; index++) {
                                if (parameterTypes[index].isInstance(parent)) {
                                    args[index] = parent;
                                } else {
                                    args[index] = null;
                                }
                            }
                            final boolean isStatic = Modifier.isStatic(method.getModifiers());
                            final ViewGroup toolbar = (ViewGroup) method.invoke(isStatic ? null : toolbarClass.newInstance(), args);
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void statusColor(Object user, Context context, TitleCompat titleCompat) {
        if (titleCompat != null && context != null) {
            final String classname = getToolbarClass(context);
            if (!TextUtils.isEmpty(classname)) {
                try {
                    final Class<?> toolbarClass = Class.forName(classname);
                    for (Method method : toolbarClass.getDeclaredMethods()) {
                        final IStatusColor statusColor = method.getAnnotation(IStatusColor.class);
                        if (statusColor != null) {
                            boolean useHere = statusColor.include().length == 0;//为空即任何类都可以使用
                            if (!useHere) {
                                for (Class clazz : statusColor.include()) {//不为空则判断是否满足之类的类型
                                    useHere = useHere || clazz.isInstance(user);
                                }
                            }
                            if (useHere) {//如果符合了满足条件，那么判断是否被排除了
                                for (Class clazz : statusColor.exclude()) {
                                    if (clazz.isInstance(user)) {
                                        useHere = false;
                                        break;
                                    }
                                }
                            }
                            if (useHere) {
                                if (!method.isAccessible()) method.setAccessible(true);

                                final Class<?>[] parameterTypes = method.getParameterTypes();
                                Object[] args = new Object[parameterTypes.length];
                                for (int index = 0, length = parameterTypes.length; index < length; index++) {
                                    if (parameterTypes[index].isInstance(context)) {
                                        args[index] = context;
                                    } else {
                                        args[index] = null;
                                    }
                                }

                                final boolean isStatic = Modifier.isStatic(method.getModifiers());
                                final Integer color = (Integer) method.invoke(isStatic ? null : toolbarClass.newInstance(), args);

                                if (color != null && color != 0) {
                                    final IStatusColor.DarkOpera darkOpera = statusColor.darkOpera();
                                    if (darkOpera == IStatusColor.DarkOpera.AUTO) {
                                        final boolean darkColor = TitleCompat.FlymeStatusbarColorUtils.isBlackColor(color, 120);
                                        //上面这个判断是判断颜色是否是深色，所以状态栏就跟颜色相反
                                        titleCompat.setStatusBarMode(!darkColor);
                                    } else if (darkOpera == IStatusColor.DarkOpera.DARK) {
                                        titleCompat.setStatusBarMode(true);
                                    } else if (darkOpera == IStatusColor.DarkOpera.LIGHT) {
                                        titleCompat.setStatusBarMode(false);
                                    }
                                    titleCompat.setFakeStatusBarColor(color);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
