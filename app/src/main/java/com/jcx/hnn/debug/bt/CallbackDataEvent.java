package com.jcx.hnn.debug.bt;

import com.frame.core.bluetooth.core.BluetoothGattChannel;
import com.frame.core.bluetooth.model.BluetoothLeDevice;

/**
 * Created by yzd on 2018/6/15 0015.
 */

public class CallbackDataEvent {
    private byte[] data;
    private boolean isSuccess;
    private BluetoothLeDevice bluetoothLeDevice;
    private BluetoothGattChannel bluetoothGattChannel;

    public CallbackDataEvent setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public CallbackDataEvent setData(byte[] data) {
        this.data = data;
        return this;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public BluetoothLeDevice getBluetoothLeDevice() {
        return bluetoothLeDevice;
    }

    public CallbackDataEvent setBluetoothLeDevice(BluetoothLeDevice bluetoothLeDevice) {
        this.bluetoothLeDevice = bluetoothLeDevice;
        return this;
    }

    public BluetoothGattChannel getBluetoothGattChannel() {
        return bluetoothGattChannel;
    }

    public CallbackDataEvent setBluetoothGattChannel(BluetoothGattChannel bluetoothGattChannel) {
        this.bluetoothGattChannel = bluetoothGattChannel;
        return this;
    }
}
