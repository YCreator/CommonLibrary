package com.jcx.hnn.debug.bt;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.frame.core.adapter.BaseRvAdapter;
import com.frame.core.interf.AdapterItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by yzd on 2018/6/7 0007.
 */

public class BluetoothHelper {

    private BaseRvAdapter<BluetoothDevice> mAdapter;
    private List<BluetoothDevice> list;
    private BluetoothAdapter adapter;
    private UUID[] uuids = new UUID[]{};
    private Context mContext;
    @SuppressLint("StaticFieldLeak")
    private static BluetoothHelper helper;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private BluetoothHelper(Context mContext) {
        this.mContext = mContext;
        init();
    }

    private void init() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && !adapter.isEnabled() && !adapter.enable()) {
            return;
        }
        list = new ArrayList<>();
        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        list.addAll(pairedDevices);
        initBroadcast();
    }


    public void search() {
        if (adapter == null) return;
        /*if (adapter.isEnabled()) {
            adapter.cancelDiscovery();
            adapter.startDiscovery();
        }*/
        if (adapter.isEnabled()) {
            mHandler.postDelayed(this::stopSearch, 10000);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
                scanner.startScan(scanCallback);
            } else {
                adapter.startLeScan(leScanCallback);
            }
        }
    }

    public void stopSearch() {
        if (adapter != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
                scanner.stopScan(scanCallback);
            } else {
                adapter.stopLeScan(leScanCallback);
            }

        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = (device, rssi, scanRecord) -> findDevice(device);

    private ScanCallback scanCallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            findDevice(result.getDevice());
        }
    };

    private void findDevice(BluetoothDevice device) {
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            if (device.getBluetoothClass().getMajorDeviceClass() == 1536) {
                boolean flag = false;
                for (BluetoothDevice bluetoothbean : list) {
                    if (bluetoothbean.getAddress().equals(device.getAddress())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    if (mAdapter != null) {
                        mAdapter.addItem(device);
                    } else {
                        list.add(device);
                    }
                }
            }
        }
    }

    public void initBroadcast() {
        String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(ACTION_PAIRING_REQUEST);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mContext.registerReceiver(mReceiver, intent);
    }

    public void uninitBroadcast() {
        mContext.unregisterReceiver(mReceiver);
    }

    public void destroy() {
        uninitBroadcast();
        mContext = null;
        if (adapter != null) {
            adapter.cancelDiscovery();
            adapter = null;
        }
        mAdapter = null;
        if (list != null) {
            list.clear();
            list = null;
        }

    }

    public void setBluetoothItem(AdapterItem<BluetoothDevice> item) {
        mAdapter = new BaseRvAdapter<BluetoothDevice>(list) {
            @NonNull
            @Override
            public AdapterItem<BluetoothDevice> onCreateItem(int viewType) {
                return item;
            }
        };
    }

    public BaseRvAdapter<BluetoothDevice> getmAdapter() {
        return mAdapter;
    }

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        BluetoothGattCharacteristic bluetoothGattCharacteristic = null;

        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {

        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {

        }

        /**
         * GATT客户端连接或断开到远程的时候
         * @param gatt      GATT客户端
         * @param status    连接或者断开操作时的状态 {BluetoothGatt#GATT_SUCCESS} 如果操作成功
         * @param newState  返回新的连接状态 {BluetoothProfile#STATE_DISCONNECTED}或者{BluetoothProfile#STATE_CONNECTED}
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

        }

        /**
         * 远程服务列表时调用的回调函数，远程设备的 characteristics 和 descriptors 已经更新，已经发现了新服务。
         * @param gatt      调用了{BluetoothGatt#discoverServices}的GATT客户端
         * @param status    如果远程设备已经成功探索，状态为{BluetoothGatt#GATT_SUCCESS}
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> list = gatt.getServices();
            for (BluetoothGattService service : list) {
                String str = service.getUuid().toString();
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    bluetoothGattCharacteristic = characteristic;
                }
            }
            enableNotification(true, gatt, bluetoothGattCharacteristic);
        }

        /**
         * 报告一个 characteristic 读取操作的结果
         * @param gatt              调用了{BluetoothGatt#readCharacteristic}的GATT客户端
         * @param characteristic    从关联的远程设备读取的 Characteristic
         * @param status            如果读取操作成功完成，状态为{BluetoothGatt#GATT_SUCCESS}
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d("BT:onCharacteristicRead", "callback characteristic read status " + status + " in thread " + Thread.currentThread());
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BT:onCharacteristicRead", Arrays.toString(characteristic.getValue()));
            }
        }

        /**
         * characteristic写入操作结果的回调.
         * @param gatt              调用了{BluetoothGatt#writeCharacteristic}的GATT客户端
         * @param characteristic    写入关联的远程设备的 Characteristic
         * @param status            写入操作的结果，如果操作成功，状态为{BluetoothGatt#GATT_SUCCESS}
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

        }

        /**
         * 由于远程 characteristic 通知而触发的回调
         * @param gatt              GATT client the characteristic is associated with
         * @param characteristic    由于远程通知事件而更新的 Characteristic
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {

        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {

        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {

        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {

        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {

        }

        private void enableNotification(boolean enable, BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (gatt == null || characteristic == null)
                return; //这一步必须要有 否则收不到通知
            gatt.setCharacteristicNotification(characteristic, enable);
        }
    };

    public void connect(BluetoothDevice device) {
        device.connectGatt(mContext, true, bluetoothGattCallback);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            BluetoothDevice device = null;
            // 搜索设备时，取得设备的MAC地址
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                /*if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    if (device.getBluetoothClass().getMajorDeviceClass() == 1536) {
                        boolean flag = false;
                        for (BluetoothDevice bluetoothbean : list) {
                            if (bluetoothbean.getAddress().equals(device.getAddress())) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            if (mAdapter != null) {
                                mAdapter.addItem(device);
                            } else {
                                list.add(device);
                            }
                        }
                    }
                }*/
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.d("BlueToothTestActivity", "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d("BlueToothTestActivity", "完成配对");
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d("BlueToothTestActivity", "取消配对");
                    default:
                        break;
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                setProgressBarIndeterminateVisibility(false);
//                setTitle("设备列表");
//                if (mNewDevicesArrayAdapter.getCount() == 0) { }
            }
        }
    };

    public static BluetoothHelper with(Context mContext) {
        if (helper == null || helper.mContext == null) {
            helper = new BluetoothHelper(mContext);
        }
        return helper;
    }

}
