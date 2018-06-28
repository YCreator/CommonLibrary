package com.frame.aop;

import android.annotation.SuppressLint;

import com.frame.aop.annotation.Trace;
import com.frame.aop.tools.Preconditions;
import com.frame.aop.tools.StopWatch;
import com.frame.core.util.utils.LogUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 方法耗时统计切面类
 * Created by yzd on 2018/5/24 0024.
 */
@Aspect
public class TraceAspect {

    private static final String POINTCUT_METHOD = "execution(@com.frame.aop.annotation.Trace * *(..))";

    private static final String POINTCUT_CONSTRUCTOR = "execution(@com.frame.aop.annotation.Trace *.new(..))";

    private static final int ns = 1000*1000;

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithTrace() {
    }

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotatedTrace() {
    }

    @Around("methodAnnotatedWithTrace() || constructorAnnotatedTrace()")
    public Object traceMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Trace trace = methodSignature.getMethod().getAnnotation(Trace.class);
        if (trace!=null && !trace.enable()) {
            return joinPoint.proceed();
        }

        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();

        if (Preconditions.isBlank(className)) {
            className = "Anonymous class";
        }

        LogUtils.i("Aop:"+className, buildLogMessage(methodName, stopWatch.getElapsedTime()));

        return result;
    }

    /**
     * Create a log message.
     *
     * @param methodName A string with the method name.
     * @param methodDuration Duration of the method in milliseconds.
     * @return A string representing message.
     */
    @SuppressLint("DefaultLocale")
    private static String buildLogMessage(String methodName, long methodDuration) {

        if (methodDuration > 10 * ns) {
            return String.format("%s() take %d ms", methodName, methodDuration / ns);
        } else if (methodDuration > ns) {
            return String.format("%s() take %dms %dns", methodName, methodDuration / ns,
                    methodDuration % ns);
        } else {
            return String.format("%s() take %dns", methodName, methodDuration % ns);
        }
    }
}
