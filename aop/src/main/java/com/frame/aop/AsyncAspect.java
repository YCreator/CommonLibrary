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
 * 异步切面类
 * 在使用AspectJ的时候可能会出现java.lang.VerifyError: Verifier rejected class
 * 异常，可能是因为定义的切面类里面的虚拟方法无法引用私有方法，
 * 所以解决这个问题的关键在于把私有访问控制符(private)替换为其他访问控制符
 * 如下例子：虚拟方法doAsyncMethod所执行的方法asyncMethod,该方法不能是私有方法，否则
 * 会出现上述问题（注：其他切面类也必须注意这个问题）。在使用AspectJ若编译不通过，可能是
 * 切面类里面，注解里所使用的AspectJ表达式有问题所导致。也应注意检查
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

    void asyncMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
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
