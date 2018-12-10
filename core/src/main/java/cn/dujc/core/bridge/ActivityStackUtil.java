package cn.dujc.core.bridge;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArraySet;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * activity管理栈
 * Created by du on 2017/9/21.
 */
public class ActivityStackUtil {

    //private final Map<Activity, Set<Fragment>> mActivityFragments = new ArrayMap<Activity, Set<Fragment>>();
    private final Set<Activity> mActivities = new ArraySet<Activity>();
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

    public void initApp(Application app) {
        app.registerActivityLifecycleCallbacks(mLifecycleCallbacks);
    }

    public void unBind(Application app) {
        app.unregisterActivityLifecycleCallbacks(mLifecycleCallbacks);
    }

    private void addActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            mActivities.add(activity);
        }
    }

    public int foregroundCount() {
        return mActivities.size();
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

    public void clearActivities() {
        mActivities.clear();
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
        }
        removeActivity(activity);
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

    //事件接受对象
    public static final byte ACTIVITY = 0b10, FRAGMENT = 0b01, ALL = 0b11;

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
            if (activity instanceof FragmentActivity) {
                if ((receiver & FRAGMENT) == FRAGMENT) {
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
}
