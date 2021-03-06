package com.frame.aop.annotation;

import android.annotation.TargetApi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Spring Cache风格的Cache注解,将结果放于缓存中|只适用于android4.0以后
 * Created by yzd on 2018/5/22 0022.
 */
@TargetApi(14)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    String key();

    int expiry() default -1; // 过期时间,单位是秒
}
