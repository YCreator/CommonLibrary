package com.jcx.hnn.debug.bt;

import com.frame.core.bluetooth.core.BluetoothGattChannel;
import com.frame.core.bluetooth.model.BluetoothLeDevice;

/**
 * Created by yzd on 2018/6/15 0015.
 */

public class NotifyDataEvent {
    private byte[] data;
    private BluetoothLeDevice bluetoothLeDevice;
    private BluetoothGattChannel bluetoothGattChannel;

    public byte[] getData() {
        return data;
    }

    public NotifyDataEvent setData(byte[] data) {
        this.data = data;
        return this;
    }

    public BluetoothLeDevice getBluetoothLeDevice() {
        return bluetoothLeDevice;
    }

    public NotifyDataEvent setBluetoothLeDevice(BluetoothLeDevice bluetoothLeDevice) {
        this.bluetoothLeDevice = bluetoothLeDevice;
        return this;
    }

    public BluetoothGattChannel getBluetoothGattChannel() {
        return bluetoothGattChannel;
    }

    public NotifyDataEvent setBluetoothGattChannel(BluetoothGattChannel bluetoothGattChannel) {
        this.bluetoothGattChannel = bluetoothGattChannel;
        return this;
    }
}
