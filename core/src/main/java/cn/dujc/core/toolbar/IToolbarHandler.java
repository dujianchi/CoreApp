package cn.dujc.core.toolbar;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

import cn.dujc.core.R;
import cn.dujc.core.util.ContextUtil;

/**
 * @author du
 * date 2018/5/12 下午7:07
 */
public final class IToolbarHandler {
    private IToolbarHandler() { }

    public static Toolbar handleByClassname(Object obj, ViewGroup parent, String classname) {
        if (!TextUtils.isEmpty(classname)) {
            try {
                final Class<?> toolbarClass = Class.forName(classname);
                for (Method method : toolbarClass.getDeclaredMethods()) {
                    final IToolbar iToolbar = method.getAnnotation(IToolbar.class);
                    if (iToolbar != null) {
                        boolean useHere = iToolbar.include().length == 0;//为空即任何类都可以使用
                        if (!useHere) {
                            for (Class clazz : iToolbar.include()) {//不为空则判断是否满足之类的类型
                                useHere = useHere || clazz.isInstance(obj);
                            }
                        }
                        if (useHere) {//如果符合了满足条件，那么判断是否被排除了
                            for (Class clazz : iToolbar.exclude()) {
                                if (clazz.isInstance(obj)) {
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
                            final Toolbar toolbar = (Toolbar) method.invoke(null, args);
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
}
