package com.frame.aop.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;

/**
 * 用于追踪某个方法花费的时间,可以用于性能调优的评判|支持追踪匿名内部类中的方法
 * Created by yzd on 2018/5/24 0024.
 */
@Target({METHOD, CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {

    boolean enable() default true;
}
