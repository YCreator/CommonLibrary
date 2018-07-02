package com.frame.aop;

import com.frame.aop.annotation.PrefsRead;
import com.frame.core.util.SPrefsUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 *
 * Created by yzd on 2018/6/19 0019.
 */
@Aspect
public class PrefsReadAspect {

    @Around("execution(!synthetic * *(..)) && onPrefsReadMethod()")
    public Object doPrefsReadMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        return prefsReadMethod(joinPoint);
    }

    @Pointcut("@within(com.frame.aop.annotation.PrefsRead)||@annotation(com.frame.aop.annotation.PrefsRead)")
    public void onPrefsReadMethod() {

    }

    Object prefsReadMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        PrefsRead prefsRead = method.getAnnotation(PrefsRead.class);

        Object result = null;
        if (prefsRead != null) {
            String key = prefsRead.key();
            String prefsName = prefsRead.prefsName();
            boolean decode = prefsRead.decode();
            result = joinPoint.proceed();
            String type = signature.getReturnType().toString();

            if (!"void".equalsIgnoreCase(type)) {
                String className = signature.getReturnType().getCanonicalName();
                SPrefsUtil appPrefs = SPrefsUtil.getInstance(prefsName);
                if ("int".equals(className) || "java.lang.Integer".equals(className)) {
                    result = appPrefs.loadIntSharedPreference(key);
                } else if ("boolean".equals(className) || "java.lang.Boolean".equals(className)) {
                    result = appPrefs.loadBooleanSharedPreference(key);
                } else if ("float".equals(className) || "java.lang.Float".equals(className)) {
                    result = appPrefs.loadFloatSharedPreference(key);
                } else if ("long".equals(className) || "java.lang.Long".equals(className)) {
                    result = appPrefs.loadLongSharedPreference(key);
                } else if ("java.lang.String".equals(className)) {
                    result = decode ? appPrefs.decryptLoad(key) : appPrefs.load(key, "");
                }
            }
        } else {
            // 不影响原来的流程
            result = joinPoint.proceed();
        }

        return result;
    }
}
