package com.mohistmc.miraimbot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 让指令不出现在帮助列表
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface NoShow {

    /**
     * @return op是否能看到
     */
    boolean opCan() default true;
}
