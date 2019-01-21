package cn.dujc.core.bridge;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * activity管理栈
 * Created by du on 2017/9/21.
 */
public class ActivityStackUtil {

    //事件接受对象
    public static final byte ACTIVITY = 0b10, FRAGMENT = 0b01, ALL = 0b11;

    //private final Map<Activity, Set<Fragment>> mActivityFragments = new ArrayMap<Activity, Set<Fragment>>();
    private final Stack<Activity> mActivities = new Stack<Activity>();
    private final Application.ActivityLifecycleCallbacks mLifecycleCallbacks;

    private static ActivityStackUtil sInstance = null;

    private ActivityStackUtil() {
        mLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) { }

            @Override
            public void onActivityResumed(Activity activity) { }

            @Override
            public void onActivityPaused(Activity activity) { }

            @Override
            public void onActivityStopped(Activity activity) { }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }

            @Override
            public void onActivityDestroyed(Activity activity) {
                removeActivity(activity);
            }
        };
    }

    public static ActivityStackUtil getInstance() {
        if (sInstance == null) {
            synchronized (ActivityStackUtil.class) {
                if (sInstance == null) {
                    sInstance = new ActivityStackUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * 向Activity中onEvent(int flag, Object value)发送事件
     *
     * @param receiver 接受者，此处为Activity和fragment
     * @param flag     标志参数
     * @param value    参数
     */
    private static void onEvent(Object receiver, int flag, Object value) {
        if (receiver instanceof IEvent) {
            ((IEvent) receiver).onMyEvent(flag, value);
        }
    }

    /**
     * 初始化方法，唯一的初始化方法
     */
    public void initApp(Application app) {
        app.registerActivityLifecycleCallbacks(mLifecycleCallbacks);
    }

    /**
     * 反注册方法，基本上用不到这个方法……
     */
    public void unBind(Application app) {
        app.unregisterActivityLifecycleCallbacks(mLifecycleCallbacks);
    }

    /**
     * 添加activity。这个方法无需在外界中访问
     */
    private void addActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            mActivities.add(activity);
        }
    }

    /**
     * 获取activity栈
     */
    public Stack<Activity> getActivities() {
        return mActivities;
    }

    /**
     * 当前注册到管理栈的activity数量
     */
    public int foregroundCount() {
        return mActivities.size();
    }

    /**
     * 当前管理栈顶部的activity
     */
    @Nullable
    public Activity topActivity() {
        final Activity activity;
        if (!ActivityStackUtil.getInstance().getActivities().isEmpty()
                && (activity = ActivityStackUtil.getInstance().getActivities().peek()) != null
                && !activity.isFinishing()) return activity;
        return null;
    }

//    public void addFragment(Activity activity, Fragment fragment) {
//        Set<Fragment> fragments = mActivityFragments.get(activity);
//        if (fragments == null) {
//            fragments = new ArraySet<Fragment>();
//            mActivityFragments.put(activity, fragments);
//        }
//        if (!fragments.contains(fragment)) {
//            fragments.add(fragment);
//        }
//    }

//    public void removeFragment(Activity activity, Fragment fragment) {
//        Set<Fragment> fragments = mActivityFragments.get(activity);
//        if (fragments != null && fragments.contains(fragment)) {
//            fragments.remove(fragment);
//        }
//    }

//    public void removeFragments(Activity activity) {
//        Set<Fragment> fragments = mActivityFragments.get(activity);
//        if (fragments != null) {
//            fragments.clear();
//            mActivityFragments.remove(activity);
//        }
//    }

    /**
     * 清除管理栈
     * @deprecated 如果脑袋不清楚，最好不要调用此方法
     */
    @Deprecated
    public void clearActivities() {
        mActivities.clear();
    }

    /**
     * 关闭指定activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            removeActivity(activity);
        }
    }

    /**
     * 关闭指定activity
     */
    public void finishActivity(Class<? extends Activity> clazz) {
        final Iterator<Activity> iterator = mActivities.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getClass().equals(clazz)) {
                iterator.remove();
                if (!activity.isFinishing()) activity.finish();
            }
        }
    }

    /**
     * 关闭全部activity
     */
    public void closeAllActivity() {
        closeAllExcept((Activity) null);
    }

    /**
     * 关闭所有Activity，除了某个Activity的类，最终可能会存在多个
     *
     * @param clazz Activity.class
     */
    public void closeAllExcept(Class<? extends Activity> clazz) {
        final Iterator<Activity> iterator = mActivities.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (!activity.getClass().equals(clazz)) {
                iterator.remove();
                activity.finish();
            } else if (activity.isFinishing()) {
                iterator.remove();
            }
        }
    }

    /**
     * 关闭所有Activity，除了某个Activity的实例，最终只会存在一个
     *
     * @param activity activity.this
     */
    public void closeAllExcept(Activity activity) {
        final Iterator<Activity> iterator = mActivities.iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();
            if (activity != next) {
                iterator.remove();
                next.finish();
            } else if (next.isFinishing()) {
                iterator.remove();
            }
        }
    }

    /**
     * 从管理栈中移除指定activity。并不一定会关闭activity，只是移出管理栈
     */
    private void removeActivity(Activity activity) {
        mActivities.remove(activity);
        //removeFragments(activity);
    }

    /**
     * 发送事件
     *
     * @param flag     标注
     * @param value    携带参数
     * @param receiver 接受对象，0b10给Activity，0b01给Fragment
     */
    public void sendEvent(int flag, Object value, byte receiver) {
        for (Activity activity : mActivities) {
            if ((receiver & ACTIVITY) == ACTIVITY) {
                onEvent(activity, flag, value);
            }
            if ((receiver & FRAGMENT) == FRAGMENT && activity instanceof FragmentActivity) {
                final List<Fragment> fragments = ((FragmentActivity) activity).getSupportFragmentManager().getFragments();
                if (fragments != null && fragments.size() > 0) {
                    for (Fragment fragment : fragments) {
                        onEvent(fragment, flag, value);
                    }
                }
            }
        }
    }
}
