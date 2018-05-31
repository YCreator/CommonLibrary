package com.frame.aop;

import android.util.Log;

import com.frame.aop.annotation.HookMethod;
import com.frame.aop.tools.Preconditions;
import com.frame.aop.tools.Reflect;
import com.frame.aop.tools.ReflectException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 调试切面类
 * Created by yzd on 2018/5/25 0025.
 */
@Aspect
public class HookMethodAspect {

    private static final String POINTCUT_METHOD = "execution(@com.frame.aop.annotation.HookMethod * *(..))";

    private static final String POINTCUT_CONSTRUCTOR = "execution(@com.frame.aop.annotation.HookMethod *.new(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithHookMethod() {
    }

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotatedHookMethod() {
    }

    @Around("methodAnnotatedWithHookMethod() || constructorAnnotatedHookMethod()")
    public void hookMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        HookMethod hookMethod = method.getAnnotation(HookMethod.class);

        if (hookMethod==null) return;

        String beforeMethod = hookMethod.beforeMethod();
        String afterMethod = hookMethod.afterMethod();

        if (Preconditions.isNotBlank(beforeMethod)) {
            try {
                Reflect.on(joinPoint.getTarget()).call(beforeMethod);
            } catch (ReflectException e) {
                e.printStackTrace();
                Log.e("Aop:HookMethod", "no method "+beforeMethod);
            }
        }

        joinPoint.proceed();

        if (Preconditions.isNotBlank(afterMethod)) {
            try {
                Reflect.on(joinPoint.getTarget()).call(afterMethod);
            } catch (ReflectException e) {
                e.printStackTrace();
                Log.e("Aop:HookMethod", "no method "+afterMethod);
            }
        }
    }
}
