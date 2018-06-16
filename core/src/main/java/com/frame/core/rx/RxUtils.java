package com.frame.core.rx;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


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
    @SuppressWarnings("unchecked")
    public static <T> LifecycleTransformer<T> bindToLifecycle(Context context) {
        if (context instanceof LifecycleProvider) {
            return ((LifecycleProvider) context).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("context not the LifecycleProvider type");
        }
    }

    /**
     * 生命周期绑定
     *
     * @param lifecycle Fragment
     */
    @SuppressWarnings("unchecked")
    public static <T> LifecycleTransformer<T> bindToLifecycle(Fragment lifecycle) {
        if (lifecycle instanceof LifecycleProvider) {
            return ((LifecycleProvider) lifecycle).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("fragment not the LifecycleProvider type");
        }
    }

    /**
     * 生命周期绑定
     *
     * @param lifecycle
     * @return
     */
    public static <T, C> ObservableTransformer<T, T> bindToLifecycle(C lifecycle) {
        if (lifecycle!= null && lifecycle instanceof LifecycleProvider) {
            return ((LifecycleProvider) lifecycle).bindToLifecycle();
        }
        return upstream -> upstream;
    }

    /**
     * 线程调度器
     */
    public static <T> ObservableTransformer<T, T> schedulersTransformer() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 新线程调度
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> schedulersNewTransformer() {
        return observable -> observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }


}
