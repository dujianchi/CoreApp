package cn.dujc.extralog.eggs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 彩蛋
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Eggs {

    TriggerCondition[] trigger() default {
            TriggerCondition.TAP
            , TriggerCondition.TAP
            , TriggerCondition.TAP
            , TriggerCondition.PRESS
    };

}
