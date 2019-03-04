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
    public static final byte ACTIVITY = 0b10, FRAGMENT = 0b01, ALL = ACTIVITY | FRAGMENT;

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
    private synchronized static void onEvent(Object receiver, int flag, Object value) {
        if (receiver instanceof IEvent) {
            ((IEvent) receiver).onMyEvent(flag, value);
        }
    }

    /**
     * 初始化方法，唯一的初始化方法
     */
    public synchronized void initApp(Application app) {
        app.registerActivityLifecycleCallbacks(mLifecycleCallbacks);
    }

    /**
     * 反注册方法，基本上用不到这个方法……
     */
    public synchronized void unBind(Application app) {
        app.unregisterActivityLifecycleCallbacks(mLifecycleCallbacks);
    }

    /**
     * 添加activity。这个方法无需在外界中访问
     */
    private synchronized void addActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            mActivities.add(activity);
        }
    }

    /**
     * 获取activity栈
     */
    public synchronized Stack<Activity> getActivities() {
        return mActivities;
    }

    /**
     * 当前注册到管理栈的activity数量
     */
    public synchronized int foregroundCount() {
        return mActivities.size();
    }

    /**
     * 当前管理栈顶部的activity
     */
    @Nullable
    public synchronized Activity topActivity() {
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
    public synchronized void clearActivities() {
        mActivities.clear();
    }

    /**
     * 关闭Activity，如果是Iterator遍历的，把remove掉Iterator，否则从Stack中remove掉
     */
    private void finish(Activity activity, Iterator iterator) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
        if (iterator != null) {
            iterator.remove();
        } else {
            removeActivity(activity);
        }
    }

    /**
     * 关闭指定activity
     */
    public synchronized void finishActivity(Activity activity) {
        finish(activity, null);
    }

    /**
     * 关闭指定activity
     */
    @SafeVarargs
    public synchronized final void finishActivity(Class<? extends Activity>... classes) {
        if (classes == null || classes.length == 0) return;
        final Iterator<Activity> iterator = mActivities.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            for (Class<? extends Activity> clazz : classes) {
                if (activity.getClass().equals(clazz)) {
                    finish(activity, iterator);
                }
            }
        }
    }

    /**
     * 关闭相同的类，但保留当前。用于关闭多开的Activity
     */
    public synchronized final void finishSameButThis(Activity lastSurvivalOfSpecies) {
        if (lastSurvivalOfSpecies == null) return;
        final Class<? extends Activity> exterminatedSpecies = lastSurvivalOfSpecies.getClass();
        final Iterator<Activity> iterator = mActivities.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity != lastSurvivalOfSpecies && activity.getClass().equals(exterminatedSpecies)) {
                finish(activity, iterator);
            }
        }
    }

    /**
     * 关闭全部activity
     */
    public synchronized void closeAllActivity() {
        closeAllExcept((Activity) null);
    }

    /**
     * 关闭所有Activity，除了某个Activity的类，最终可能会存在多个
     *
     * @param classes Activity.class
     */
    @SafeVarargs
    public synchronized final void closeAllExcept(Class<? extends Activity>... classes) {
        if (classes == null || classes.length == 0) return;
        final Iterator<Activity> iterator = mActivities.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            boolean contain = false;
            for (Class<? extends Activity> clazz : classes) {
                contain = contain || activity.getClass().equals(clazz);
            }
            if (!contain) {
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
    public synchronized void closeAllExcept(Activity activity) {
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
    private synchronized void removeActivity(Activity activity) {
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
    public synchronized void sendEvent(int flag, Object value, byte receiver) {
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
