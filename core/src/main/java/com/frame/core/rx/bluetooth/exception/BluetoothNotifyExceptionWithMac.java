package com.frame.core.rx.bluetooth.exception;

/**
 * Created by Billin on 2017/4/14.
 */
public class BluetoothNotifyExceptionWithMac extends BluetoothExceptionWithMac {

    public BluetoothNotifyExceptionWithMac(String msg, String mac) {
        super(msg, mac);
    }
}
