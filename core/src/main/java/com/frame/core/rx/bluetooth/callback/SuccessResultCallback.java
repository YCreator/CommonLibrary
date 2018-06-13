package com.frame.core.rx.bluetooth.callback;

/**
 * 简化 Callback 的错误回调
 * Created by yzd on 2018/6/13 0013.
 */

public abstract class SuccessResultCallback<D> implements BaseResultCallback<D> {

    private BaseResultCallback errorCallback;

    public SuccessResultCallback(BaseResultCallback errorCallback) {
        this.errorCallback = errorCallback;
    }

    @Override
    public void onFail(String msg) {
        errorCallback.onFail(msg);
    }
}
