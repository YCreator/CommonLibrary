package com.lib.commonlibrary;

import com.frame.core.util.lifeful.Lifeful;
import com.frame.core.util.TLog;
import com.google.gson.JsonSyntaxException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by yzd on 2017/5/9 0009.
 */

public abstract class JsonDataObserver<T> implements Observer<JsonDataEntity<T>> {
    @Override
    public void onSubscribe(Disposable d) {
        if (lifeful() != null && !lifeful().isAlive()) {
            d.dispose();
        }
        TLog.i("onSubscribe");
    }

    @Override
    public void onNext(JsonDataEntity<T> value) {
        TLog.i("onNext");
        if (lifeful() == null || lifeful().isAlive()) {
            if (value.isSuccess()) {
                onSuccess(value.getData());
            } else {
                onError(0, value.getMessage());
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (lifeful() == null || lifeful().isAlive()) {
            onError(-1, getErrorMessage(e));
        }
    }

    @Override
    public void onComplete() {
        TLog.i("onComplete");
    }

    private String getErrorMessage(Throwable e) {
        if (e instanceof JsonSyntaxException) {
            return "数据异常";
        }
        return e.toString();
    }

    abstract void onSuccess(T t);

    abstract void onError(int errorCode, String message);

    abstract Lifeful lifeful();
}
