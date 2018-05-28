package com.frame.core.rx.bus;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by yzd on 2018/5/28 0028.
 */

public class ObjectDisposable {

    private Object obj;
    private CompositeDisposable disposable;

    public ObjectDisposable(Object obj, CompositeDisposable disposable) {
        this.obj = obj;
        this.disposable = disposable;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public CompositeDisposable getDisposable() {
        return disposable;
    }

    public void setDisposable(CompositeDisposable disposable) {
        this.disposable = disposable;
    }
}
