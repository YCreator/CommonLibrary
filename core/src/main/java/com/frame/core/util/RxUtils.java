package com.frame.core.util;

import android.content.Context;

import com.frame.core.net.ExceptionHandle;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by yzd on 2018/3/3 0003.
 * 有关Rx的工具类
 */

public class RxUtils {
    /**
     * 生命周期绑定
     *
     * @param context Activity或者Fragment
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(Context context) {
        if (context instanceof LifecycleProvider) {
            return ((LifecycleProvider) context).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("context not the LifecycleProvider type");
        }
    }

    /**
     * 线程调度器
     */
    /*public static Observable.Transformer schedulersTransformer() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }*/



    private static class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override
        public Observable<T> call(Throwable t) {
            return Observable.error(ExceptionHandle.handleException(t));
        }
    }

}
