package cn.dujc.core.toolbar;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author du
 * date 2018/5/12 下午6:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IStatusColor {

    enum DarkOpera {
        DO_NOT//不操作
        , AUTO//自动
        , DARK//深色
        , LIGHT//浅色
    }

    /**
     * 是否是深色的
     */
    DarkOpera darkOpera() default DarkOpera.DO_NOT;
}
