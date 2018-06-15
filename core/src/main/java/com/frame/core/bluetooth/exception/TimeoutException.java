package com.frame.core.bluetooth.exception;


import com.frame.core.bluetooth.common.BleExceptionCode;

/**
 * @Description: 超时异常
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 16/8/14 10:29.
 */
public class TimeoutException extends BleException {
    public TimeoutException() {
        super(BleExceptionCode.TIMEOUT, "Timeout Exception Occurred! ");
    }
}
