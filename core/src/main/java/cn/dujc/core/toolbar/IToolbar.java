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
public @interface IToolbar {

    Class[] include() default {};//用来设置不同toolbar的，当符合被设置的class的才能使用
    Class[] exclude() default {};//用来排除是否使用此注解的

    //注解toolbar的类，需要在静态方法中注解，样式如下：
    /*
    @IToolbar
    public static Toolbar normal(ViewGroup parent){
        return toolbar;
    }
     */
}
