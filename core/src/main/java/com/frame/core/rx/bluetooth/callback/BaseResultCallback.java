package com.frame.core.rx.bluetooth.callback;

/**
 * 用于数据回传的基本回调接口
 * Created by yzd on 2018/6/13 0013.
 */

public interface BaseResultCallback<D> {

    /**
     * 成功拿到数据
     *
     * @param data 回传的数据
     */
    void onSuccess(D data);

    /**
     * 操作失败
     *
     * @param msg 失败的返回的异常信息
     */
    void onFail(String msg);
}
