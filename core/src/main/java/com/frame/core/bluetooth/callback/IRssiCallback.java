package com.frame.core.bluetooth.callback;


import com.frame.core.bluetooth.exception.BleException;

/**
 * @Description: 获取信号值回调
 * @author: <a href="http://xiaoyaoyou1212.360doc.com">DAWI</a>
 * @date: 2017/10/19 15:19
 */
public interface IRssiCallback {
    void onSuccess(int rssi);

    void onFailure(BleException exception);
}
