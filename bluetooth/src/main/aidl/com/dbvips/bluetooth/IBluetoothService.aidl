// IBluetoothManager.aidl
package com.dbvips.bluetooth;

// Declare any non-default types here with import statements

import com.dbvips.bluetooth.IResponse;

interface IBluetoothService {
    void callBluetoothApi(int code, inout Bundle args, IResponse response);
}
