package com.frame.aop.annotation;

import android.annotation.TargetApi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yzd on 2018/5/22 0022.
 */
@TargetApi(14)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Cacheable {

    String key();

    int expiry() default -1; // 过期时间,单位是秒
}
