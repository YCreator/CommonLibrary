package com.dbvips.bluetooth.connect.listener;

import com.dbvips.bluetooth.model.BleGattProfile;

/**
 * Created by dingjikerbo on 2016/8/25.
 */
public interface ServiceDiscoverListener extends GattResponseListener {
    void onServicesDiscovered(int status, BleGattProfile profile);
}
