package com.mohistmc.miraimbot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实现权限的注册
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Permission {
    /**
     * @return 权限名称
     */
    String value();
}
