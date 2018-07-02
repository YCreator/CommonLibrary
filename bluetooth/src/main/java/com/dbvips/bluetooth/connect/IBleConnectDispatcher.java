package com.dbvips.bluetooth.connect;

import com.dbvips.bluetooth.connect.request.BleRequest;

public interface IBleConnectDispatcher {

    void onRequestCompleted(BleRequest request);
}
