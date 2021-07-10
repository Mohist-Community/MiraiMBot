package com.mohistmc.miraimbot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实现指令的注册
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Command {
    /**
     * @return 指令名称
     */
    String value();

    /**
     * @return 指令所有别称
     */
    String[] alias() default {};

    /**
     * @return 指令用法
     */
    String usage() default "";

    /**
     * @return 指令简介
     */
    String description() default "";

    /**
     * @return 限定指令只能在某个场景下使用
     */
    Type type() default Type.ALL;

    public enum Type {
        FRIEND,
        GROUP,
        CONSOLE,
        ALL
    }
}
