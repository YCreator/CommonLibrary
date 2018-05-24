package com.frame.aop;

import android.annotation.TargetApi;

import com.frame.aop.annotation.Cacheable;
import com.frame.core.util.cache.Cache;
import com.frame.core.util.utils.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by yzd on 2018/5/22 0022.
 */
@TargetApi(14)
@Aspect
public class CacheableAspect {

    @Around("execution(!synthetic * *(..)) && onCacheMethod()")
    public Object doCacheMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        return cacheMethod(joinPoint);
    }

    @Pointcut("@within(com.frame.aop.annotation.Cacheable) || @annotation(com.frame.aop.annotation.Cacheable)")
    public void onCacheMethod() {

    }

    private Object cacheMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        @SuppressWarnings("ReflectionForUnavailableAnnotation")
        Cacheable cacheable = method.getAnnotation(Cacheable.class);
        Object result = null;
        if (cacheable != null) {
            String key = cacheable.key();
            int expiry = cacheable.expiry();
            result = joinPoint.proceed();
            Cache cache = Cache.get(Utils.getApp());
            if (expiry > 0) {
                cache.put(key, (Serializable) result, expiry);
            } else {
                cache.put(key, (Serializable) result);
            }
        } else {
            result = joinPoint.proceed();
        }
        return result;
    }
}
