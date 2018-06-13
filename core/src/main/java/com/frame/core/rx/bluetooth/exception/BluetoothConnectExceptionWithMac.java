package com.frame.core.rx.bluetooth.exception;

/**
 * 蓝牙连接异常
 * <p/>
 * Created by Billin on 2017/4/14.
 */
public class BluetoothConnectExceptionWithMac extends BluetoothExceptionWithMac {

    public BluetoothConnectExceptionWithMac(String msg, String mac) {
        super(msg, mac);
    }
}
