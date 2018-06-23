package com.frame.core.net.Retrofit;

import android.os.Handler;
import android.os.Looper;

import com.frame.core.entity.BaseJsonEntity;
import com.frame.core.entity.JsonEntity;
import com.frame.core.util.lifeful.Lifeful;

/**
 * Created by yzd on 2017/10/16 0016.
 */

public abstract class DBaseCallback<T, E extends JsonEntity> implements DCallback<E> {

    private static CustomHandler handler;

    private boolean inUIThread;
    private Lifeful lifeful;

    public DBaseCallback() {

    }

    public DBaseCallback(boolean inUIThread) {
        this.inUIThread = inUIThread;
    }

    public DBaseCallback(boolean inUIThread, Lifeful lifeful) {
        this.inUIThread = inUIThread;
        this.lifeful = lifeful;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEntity(E value) {
       /* if (value.isSuccess()) {
            onSuccess(value.getData().getClass().getT, value);
        } else {
            onFault(value.getCode(), value.getMessage());
        }*/
    }

    @Override
    public void onError(Throwable e) {
        onFault(-1, e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    public boolean isInUIThread() {
        return inUIThread;
    }

    public boolean isLifeful() {
        return lifeful != null && lifeful.isAlive();
    }

    private static class CustomHandler extends Handler {
        private CustomHandler() {
            super(Looper.getMainLooper());
        }
    }

    public static Handler getHandler() {
        synchronized (DBaseCallback.class) {
            if (handler == null) {
                handler = new CustomHandler();
            }
            return handler;
        }
    }

    public abstract void onSuccess(T data, BaseJsonEntity<T> entity);

    public abstract void onFault(int code, String message);
}
