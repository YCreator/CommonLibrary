package com.frame.core.bluetooth.exception.handler;


import com.frame.core.bluetooth.exception.ConnectException;
import com.frame.core.bluetooth.exception.GattException;
import com.frame.core.bluetooth.exception.InitiatedException;
import com.frame.core.bluetooth.exception.OtherException;
import com.frame.core.bluetooth.exception.TimeoutException;
import com.frame.core.util.utils.LogUtils;

/**
 * @Description: 异常默认处理
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 16/8/14 10:35.
 */
public class DefaultBleExceptionHandler extends BleExceptionHandler {
    @Override
    protected void onConnectException(ConnectException e) {
        LogUtils.e(e.getDescription());
    }

    @Override
    protected void onGattException(GattException e) {
        LogUtils.e(e.getDescription());
    }

    @Override
    protected void onTimeoutException(TimeoutException e) {
        LogUtils.e(e.getDescription());
    }

    @Override
    protected void onInitiatedException(InitiatedException e) {
        LogUtils.e(e.getDescription());
    }

    @Override
    protected void onOtherException(OtherException e) {
        LogUtils.e(e.getDescription());
    }
}
