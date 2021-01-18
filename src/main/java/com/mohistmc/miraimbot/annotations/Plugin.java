package com.mohistmc.miraimbot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实现插件的注册
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Plugin {
    /**
     * @return 插件名称
     */
    String value();

    /**
     * @return 插件版本
     */
    String version() default "none";

    /**
     * @return 插件作者
     */
    String[] authors() default {"none"};

    /**
     * @return 插件简介
     */
    String description() default "none";
}
