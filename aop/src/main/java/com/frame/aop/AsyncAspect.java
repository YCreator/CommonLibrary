package com.frame.aop;

import android.os.Looper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yzd on 2018/5/22 0022.
 */
@Aspect
public class AsyncAspect {

    @Around("execution(!synthetic * *(..)) && onAsyncMethod()")
    public void doAsyncMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        asyncMethod(joinPoint);
    }

    @Pointcut("@within(com.frame.aop.annotation.Async)||@annotation(com.frame.aop.annotation.Async)")
    public void onAsyncMethod() {
    }

    private void asyncMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(FlowableEmitter<Object> e) throws Exception {
                Looper.prepare();
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Looper.loop();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
