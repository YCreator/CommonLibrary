package com.lib.commonlibrary;

import com.frame.core.rx.Lifeful;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by yzd on 2017/5/9 0009.
 */

public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T value) {
        if (lifeful().isAlive())
            onSuccess(value);
    }

    @Override
    public void onError(Throwable e) {
        if (lifeful().isAlive())
        onError(-1, e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    abstract void onSuccess(T t);

    abstract void onError(int errorCode, String message);

    abstract Lifeful lifeful();
}
