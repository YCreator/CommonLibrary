package com.frame.aop;

import android.app.Activity;
import android.app.Fragment;

import com.frame.aop.annotation.Permission;
import com.frame.core.util.PermissionHelper.PermissionHelper;

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
public class PermissionAspect {

    @Around("execution(!synthetic * *(..)) && onPermissionMethod()")
    public void doPermissionMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        permissionMethod(joinPoint);
    }

    @Pointcut("@within(com.frame.aop.annotation.Permission)||@annotation(com.frame.aop.annotation.Permission)")
    public void onPermissionMethod() {

    }

    private void permissionMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        Activity context = null;
        Object object = joinPoint.getThis();
        if (object instanceof Activity) {
            context = (Activity) object;
        } else if (object instanceof Fragment) {
            context = ((Fragment) object).getActivity();
        } else if (object instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) object).getActivity();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Permission permission = method.getAnnotation(Permission.class);
        String[] values = null;
        if (permission != null) {
            values = permission.value();
        }
        if (context == null || values == null || values.length == 0) return;

        new PermissionHelper()
                .with(context)
                .request(new PermissionHelper.OnSuccessListener() {
                    @Override
                    public void onPermissionSuccess() {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }, values);
    }
}
