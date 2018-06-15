package com.jcx.hnn.debug.bt;

import com.frame.core.bluetooth.model.BluetoothLeDeviceStore;

/**
 * Created by yzd on 2018/6/15 0015.
 */

public class ScanEvent {
    private boolean isScanTimeout;
    private boolean isScanFinish;
    private BluetoothLeDeviceStore bluetoothLeDeviceStore;

    public ScanEvent() {
    }

    public boolean isScanTimeout() {
        return isScanTimeout;
    }

    public ScanEvent setScanTimeout(boolean scanTimeout) {
        isScanTimeout = scanTimeout;
        return this;
    }

    public boolean isScanFinish() {
        return isScanFinish;
    }

    public ScanEvent setScanFinish(boolean scanFinish) {
        isScanFinish = scanFinish;
        return this;
    }

    public BluetoothLeDeviceStore getBluetoothLeDeviceStore() {
        return bluetoothLeDeviceStore;
    }

    public ScanEvent setBluetoothLeDeviceStore(BluetoothLeDeviceStore bluetoothLeDeviceStore) {
        this.bluetoothLeDeviceStore = bluetoothLeDeviceStore;
        return this;
    }
}
