package com.jcx.hnn.debug.bt;

import com.frame.core.bluetooth.core.DeviceMirror;

/**
 * Created by yzd on 2018/6/15 0015.
 */

public class ConnectEvent {
    private boolean isSuccess;
    private boolean isDisconnected;
    private DeviceMirror deviceMirror;

    public boolean isSuccess() {
        return isSuccess;
    }

    public ConnectEvent setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public boolean isDisconnected() {
        return isDisconnected;
    }

    public ConnectEvent setDisconnected(boolean disconnected) {
        isDisconnected = disconnected;
        return this;
    }

    public DeviceMirror getDeviceMirror() {
        return deviceMirror;
    }

    public ConnectEvent setDeviceMirror(DeviceMirror deviceMirror) {
        this.deviceMirror = deviceMirror;
        return this;
    }
}
