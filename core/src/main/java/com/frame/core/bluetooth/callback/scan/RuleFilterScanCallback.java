package com.frame.core.bluetooth.callback.scan;

import com.frame.core.bluetooth.Bluetooth;
import com.frame.core.bluetooth.model.BluetoothLeDevice;
import com.frame.core.bluetooth.scan.BleScanRuleConfig;
import com.frame.core.util.utils.StringUtils;

/**
 * Created by yzd on 2018/7/2 0002.
 */

public class RuleFilterScanCallback extends ScanCallback {

    private BleScanRuleConfig config;

    public RuleFilterScanCallback(IScanCallback scanCallback, BleScanRuleConfig config) {
        super(scanCallback);
        this.config = config;
    }

    @Override
    public void startLeScan() {
        if (config.getServiceUuids() != null && config.getServiceUuids().length > 0) {
            isScanning = true;
            if (Bluetooth.getInstance().getBluetoothAdapter() != null) {
                Bluetooth.getInstance().getBluetoothAdapter().startLeScan(config.getServiceUuids(), RuleFilterScanCallback.this);
            }
        } else {
            super.startLeScan();
        }
    }

    @Override
    public BluetoothLeDevice onFilter(BluetoothLeDevice bluetoothLeDevice) {
        String[] deviceNames = config.getDeviceNames();
        String mac = config.getDeviceMac();
        if (!StringUtils.isEmpty(mac) && !mac.equalsIgnoreCase(bluetoothLeDevice.getAddress())) {
            return null;
        }
        if (deviceNames != null && deviceNames.length > 0) {
            boolean isTrue = false;
            for (String deviceName : deviceNames) {
                if (deviceName.equalsIgnoreCase(bluetoothLeDevice.getName())) {
                    isTrue = true;
                }
            }
            return isTrue ? bluetoothLeDevice : null;
        }
        return super.onFilter(bluetoothLeDevice);
    }
}
