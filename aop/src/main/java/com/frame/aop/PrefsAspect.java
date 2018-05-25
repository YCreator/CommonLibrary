package com.frame.aop;

import com.frame.aop.annotation.Prefs;
import com.frame.core.util.SPrefsUtil;
import com.frame.core.util.utils.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Created by yzd on 2018/5/25 0025.
 */
@Aspect
public class PrefsAspect {

    @Around("execution(!synthetic * *(..)) && onPrefsMethod()")
    public Object doPrefsMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        return prefsMethod(joinPoint);
    }

    @Pointcut("@within(com.frame.aop.annotation.Prefs)||@annotation(com.frame.aop.annotation.Prefs)")
    public void onPrefsMethod() {

    }

    private Object prefsMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Prefs prefs = method.getAnnotation(Prefs.class);
        Object result = null;
        if (prefs != null) {
            String key = prefs.key();
            String prefsName = prefs.prefsName();
            boolean encode = prefs.encode();
            result = joinPoint.proceed();
            String type = ((MethodSignature) joinPoint.getSignature()).getReturnType().toString();

            if (!"void".equalsIgnoreCase(type)) {
                String className = ((MethodSignature) joinPoint.getSignature()).getReturnType().getCanonicalName();
                SPrefsUtil appPrefs = SPrefsUtil.getInstance(Utils.getApp(), prefsName);
                if ("int".equals(className) || "java.lang.Integer".equals(className)) {
                    appPrefs.saveSharedPreferences(key, (Integer) result);
                } else if ("boolean".equals(className) || "java.lang.Boolean".equals(className)) {
                    appPrefs.saveSharedPreferences(key, (Boolean) result);
                } else if ("float".equals(className) || "java.lang.Float".equals(className)) {
                    appPrefs.saveSharedPreferences(key, (Float) result);
                } else if ("long".equals(className) || "java.lang.Long".equals(className)) {
                    appPrefs.saveSharedPreferences(key, (Long) result);
                } else if ("java.lang.String".equals(className)) {
                    if (encode) {
                        appPrefs.encryptSave(key, (String) result);
                    } else {
                        appPrefs.save(key, (String) result);
                    }
                } else {
                    appPrefs.saveObject(key, result);
                }
            }
        } else {
            // 不影响原来的流程
            result = joinPoint.proceed();
        }

        return result;
    }
}
