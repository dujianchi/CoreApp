package cn.dujc.core.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 一些初始化类的保存类
 * @author du
 */
public class Initializer {

    private static final String NAME = Initializer.class.getSimpleName();

    private Initializer() { }

    public static SharedPreferences classesSavior(Context context) {
        return context.getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

}
