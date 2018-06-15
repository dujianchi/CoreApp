package cn.dujc.extralog.eggs;

import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class EggsHandler {

    public void setupEggsHere(View triggerView, Object annotationUser) {
        if (annotationUser == null || triggerView == null) return;
        Method[] methods = annotationUser.getClass().getDeclaredMethods();
        for (Method method : methods){
            Eggs eggs = method.getAnnotation(Eggs.class);
            if (eggs != null){
                if (!method.isAccessible()) method.setAccessible(true);
                final Class<?>[] parameterTypes = method.getParameterTypes();
                final Object[] args = new Object[parameterTypes.length];
                TriggerCondition[] conditions = eggs.trigger();
                // 彩蛋的触发，通常是设置touch事件或者点击事件，但是要如何不影响原本的点击事件是个问题
                //todo 应该在这里设置triggerView的事件，是否符合设置的条件，符合就触发对应的方法
                //todo
                //todo 以下的代码是满足条件触发的代码
                final boolean isStatic = Modifier.isStatic(method.getModifiers());
                try {
                    method.invoke(isStatic ? null : annotationUser, args);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
