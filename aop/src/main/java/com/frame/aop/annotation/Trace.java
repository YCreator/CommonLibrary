package com.frame.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yzd on 2018/5/24 0024.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Trace {

    boolean enable() default true;
}
