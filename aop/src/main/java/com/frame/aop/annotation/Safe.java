package com.frame.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可以安全地执行方法,而无需考虑是否会抛出运行时异常
 * Created by yzd on 2018/5/24 0024.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Safe {
}
