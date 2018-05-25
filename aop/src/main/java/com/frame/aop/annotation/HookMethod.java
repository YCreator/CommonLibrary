package com.frame.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可以在调用某个方法之前、以及之后进行hook|比较适合埋点的场景，可以单独使用也可以跟任何自定义注解配合使用。也支持在匿名内部类中使用
 * Created by yzd on 2018/5/25 0025.
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface HookMethod {

    String beforeMethod() default "";

    String afterMethod() default "";
}
