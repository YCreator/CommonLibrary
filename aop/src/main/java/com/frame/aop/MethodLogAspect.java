package com.frame.aop;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

/**
 * 打印方法日志切面类
 * Created by yzd on 2018/5/24 0024.
 */
@Aspect
public class MethodLogAspect {

    @Around("execution(!synthetic * *(..)) && onMethodLog()")
    public Object doMethodLog(final ProceedingJoinPoint joinPoint) throws Throwable {
        return methodLog(joinPoint);
    }

    @Pointcut("@within(com.frame.aop.annotation.MethodLog)||@annotation(com.frame.aop.annotation.MethodLog)")
    public void onMethodLog() {

    }

    Object methodLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.w("Aop:methodLog", joinPoint.getSignature().toShortString() + " 方法参数 : " + (joinPoint.getArgs() != null ? Arrays.deepToString(joinPoint.getArgs()) : ""));
        Object result = joinPoint.proceed();
        String type = ((MethodSignature) joinPoint.getSignature()).getReturnType().toString();
        Log.w("Aop:methodLog", joinPoint.getSignature().toShortString() + " 返回结果 : " + ("void".equalsIgnoreCase(type) ? "void" : result));
        return result;
    }
}
