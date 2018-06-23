package com.frame.aop.annotation;

import android.annotation.TargetApi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yzd on 2018/6/19 0019.
 */
@TargetApi(14)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrefsRead {

    String key();

    String prefsName();

    boolean  decode() default false;
}
