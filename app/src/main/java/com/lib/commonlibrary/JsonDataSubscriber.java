package com.lib.commonlibrary;

import com.frame.core.util.TLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by yzd on 2017/5/10 0010.
 */

public abstract class JsonDataSubscriber<T> implements Subscriber<JsonDataEntity<T>> {
    @Override
    public void onSubscribe(Subscription s) {
        TLog.i("onSubscribe");
        s.request(1);
    }

    @Override
    public void onNext(JsonDataEntity<T> value) {
        TLog.i("onNext");
        // if (lifeful().isAlive()) {
        if (value.isSuccess()) {
            onSuccess(value.getData());
        } else {
            onError(0, value.getMessage());
        }
        // }
    }

    @Override
    public void onError(Throwable e) {
        TLog.i(e.toString());
        // if (lifeful().isAlive()) {
        onError(-1, e.getMessage());
        // }
    }

    @Override
    public void onComplete() {
        TLog.i("onComplete");
    }

    abstract void onSuccess(T t);

    abstract void onError(int errorCode, String message);

    // abstract Lifeful lifeful();
}
