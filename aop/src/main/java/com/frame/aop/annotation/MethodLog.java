package com.frame.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将方法的入参和出参都打印出来,可以用于调试
 * Created by yzd on 2018/5/24 0024.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface MethodLog {
}
