package com.frame.aop.annotation;

import android.annotation.TargetApi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将方法返回的结果放入AppPrefs中|只适用于android4.0以后
 * Created by yzd on 2018/5/25 0025.
 */
@TargetApi(14)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Prefs {

    String key();

    String prefsName();

    boolean encode() default false;
}
