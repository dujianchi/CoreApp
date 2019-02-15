package cn.dujc.core.initializer.baselist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.app.Initializer;

/**
 * @author du
 * date 2018/5/12 下午7:07
 */
public final class IBaseListHandler {

    private static final String CLASS = "CORE_LIST_SETUP";
    private static IBaseListSetup sSetup = null;

    private IBaseListHandler() {
    }

    /**
     * 设置setup的处理类
     */
    public static void setSetupClass(Context context, Class<? extends IBaseListSetup> setupClass) {
        if (context == null || setupClass == null) return;
        final String className = setupClass.getName();
        Initializer.classesSavior(context).edit().putString(CLASS, className).apply();
        createSetupByClass(setupClass);
    }

    /**
     * 从保存的类中获取setup实例。处理顺序为 1.静态公有方法  2.注解方法  3.创建实例
     */
    private static void createSetupByClass(Class<? extends IBaseListSetup> clazz) {
        try {
            final Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                final IBaseListSetup fromStatic = createByStaticMethod(method);
                if (fromStatic != null) {
                    sSetup = fromStatic;
                    return;
                }
            }
            sSetup = createByNewInstance(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从静态公有方法获取IBaseListSetup实例
     */
    private static IBaseListSetup createByStaticMethod(Method method) {
        try {
            final boolean isStatic = Modifier.isStatic(method.getModifiers());
            if (isStatic) {
                if (!method.isAccessible()) method.setAccessible(true);
                final Object invoke = method.invoke(null);
                if (invoke instanceof IBaseListSetup) return (IBaseListSetup) invoke;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用new的方式获取IBaseListSetup实例。这是最终也是最暴力的方式，当静态类、注解都失败的时候
     * ，会执行这个方案，这个方案会尽可能地通过传递的类来强制创建一个IBaseListSetup实例
     */
    public static IBaseListSetup createByNewInstance(Class clazz) {
        final Constructor[] constructors = clazz.getDeclaredConstructors();
        AccessibleObject.setAccessible(constructors, true);
        for (Constructor constructor : constructors) {
            try {
                Object instance = constructor.newInstance();
                if (instance instanceof IBaseListSetup) {
                    return (IBaseListSetup) instance;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取IBaseListSetup的实例
     */
    public static IBaseListSetup getSetup(Context context) {
        if (sSetup != null) return sSetup;
        if (context == null) return null;
        try {
            final Class<?> setupClass = Class.forName(Initializer.classesSavior(context).getString(CLASS, ""));
            createSetupByClass((Class<? extends IBaseListSetup>) setupClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return sSetup;
    }

    public static void setup(Context context, RecyclerView recyclerView, BaseQuickAdapter adapter) {
        IBaseListSetup setup = getSetup(context);
        if (setup != null) setup.recyclerViewOtherSetup(context, recyclerView, adapter);
    }


}
