package com.dbvips.bluetooth;

import com.dbvips.bluetooth.connect.listener.BleConnectStatusListener;
import com.dbvips.bluetooth.connect.response.BleNotifyResponse;
import com.dbvips.bluetooth.receiver.listener.BluetoothBondListener;
import com.dbvips.bluetooth.connect.listener.BluetoothStateListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by liwentian on 2017/1/13.
 */

public class BluetoothClientReceiver {

    private HashMap<String, HashMap<String, List<BleNotifyResponse>>> mNotifyResponses;
    private HashMap<String, List<BleConnectStatusListener>> mConnectStatusListeners;
    private List<BluetoothStateListener> mBluetoothStateListeners;
    private List<BluetoothBondListener> mBluetoothBondListeners;
}
