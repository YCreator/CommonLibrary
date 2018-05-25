package com.frame.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 借助rxjava,异步执行app中的方法
 * Created by yzd on 2018/5/22 0022.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Async {
}
