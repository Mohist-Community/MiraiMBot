package com.mohistmc.miraimbot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 让指令只可以让op使用
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface OnlyOp {

}
