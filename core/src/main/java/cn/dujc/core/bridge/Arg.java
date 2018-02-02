package cn.dujc.core.bridge;

import android.support.annotation.Nullable;

/**
 * 传参用
 * Created by lucky on 2017/11/8.
 */
public class Arg {

    public static final Arg NONE = new Arg(0);//用在无需返回参数，只需要通知一下的情况

    private final Object value;

    public Arg(Object value) {
        this.value = value;
    }

    @Nullable
    public <T> T getValue(Class<T> clazz) {
        T v = null;
        try {
            v = clazz.cast(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
