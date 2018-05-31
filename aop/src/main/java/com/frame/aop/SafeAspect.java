package com.frame.aop;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 空指针安全切面类
 * Created by yzd on 2018/5/24 0024.
 */
@Aspect
public class SafeAspect {

    @Around("execution(!synthetic * *(..)) && onSafeMethod()")
    public Object doSafeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        return safeMethod(joinPoint);
    }

    @Pointcut("@within(com.frame.aop.annotation.Safe)||@annotation(com.frame.aop.annotation.Safe)")
    public void onSafeMethod() {

    }

    Object safeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            Log.w("Aop:safe", getStringFromException(e));
        }
        return result;
    }

    private static String getStringFromException(Throwable ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
