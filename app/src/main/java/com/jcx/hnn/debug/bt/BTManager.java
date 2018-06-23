package com.jcx.hnn.debug.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;

import com.frame.core.bluetooth.Bluetooth;
import com.frame.core.bluetooth.callback.IBleCallback;
import com.frame.core.bluetooth.callback.IConnectCallback;
import com.frame.core.bluetooth.callback.scan.IScanCallback;
import com.frame.core.bluetooth.callback.scan.ScanCallback;
import com.frame.core.bluetooth.common.PropertyType;
import com.frame.core.bluetooth.core.BluetoothGattChannel;
import com.frame.core.bluetooth.core.DeviceMirror;
import com.frame.core.bluetooth.core.DeviceMirrorPool;
import com.frame.core.bluetooth.exception.BleException;
import com.frame.core.bluetooth.model.BluetoothLeDevice;
import com.frame.core.bluetooth.model.BluetoothLeDeviceStore;
import com.frame.core.bluetooth.utils.HexUtil;
import com.frame.core.util.utils.LogUtils;
import com.lib.commonlibrary.MyApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

/**
 * Created by yzd on 2018/6/15 0015.
 */

public class BTManager {
    private static BTManager instance;
    private DeviceMirrorPool mDeviceMirrorPool;
    private ScanEvent scanEvent = new ScanEvent().setBluetoothLeDeviceStore(new BluetoothLeDeviceStore());
    private ConnectEvent connectEvent = new ConnectEvent();
    private CallbackDataEvent callbackDataEvent = new CallbackDataEvent();
    private NotifyDataEvent notifyDataEvent = new NotifyDataEvent();

    private ScanCallback scanCallback = new ScanCallback(new IScanCallback() {
        @Override
        public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
            LogUtils.i("BTManager", "onDeviceFound" + bluetoothLeDevice.toString());
            MyApplication.getHandler().post(() -> {
                scanEvent.getBluetoothLeDeviceStore().addDevice(bluetoothLeDevice);
                EventBus.getDefault().post(scanEvent.setScanFinish(false).setScanTimeout(false));
            });
        }

        @Override
        public void onScanFinish(BluetoothLeDeviceStore bluetoothLeDeviceStore) {
            LogUtils.i("BTManager", "onScanFinish");
            EventBus.getDefault().post(scanEvent.setScanFinish(true).setScanTimeout(false));
        }

        @Override
        public void onScanTimeout() {
            LogUtils.i("BTManager", "onScanTimeout");
            EventBus.getDefault().post(scanEvent.setScanFinish(true).setScanTimeout(true));
        }
    });

    /**
     * 连接回调
     */
    private IConnectCallback connectCallback = new IConnectCallback() {

        @Override
        public void onConnectSuccess(final DeviceMirror deviceMirror) {
            LogUtils.i("BTManager", "Connect Success!");
            EventBus.getDefault().post(connectEvent.setDeviceMirror(deviceMirror).setSuccess(true));
        }

        @Override
        public void onConnectFailure(BleException exception) {
            LogUtils.i("BTManager", "Connect Failure!");
            EventBus.getDefault().post(connectEvent.setSuccess(false).setDisconnected(false));
        }

        @Override
        public void onDisconnect(boolean isActive) {
            LogUtils.i("BTManager", "Disconnect!");
            EventBus.getDefault().post(connectEvent.setSuccess(false).setDisconnected(true));
        }
    };

    /**
     * 接收数据回调
     */
    private IBleCallback receiveCallback = new IBleCallback() {
        @Override
        public void onSuccess(final byte[] data, BluetoothGattChannel bluetoothGattInfo, BluetoothLeDevice bluetoothLeDevice) {
            if (data == null) {
                return;
            }
            LogUtils.i("BTManager", "notify success:" + HexUtil.encodeHexStr(data));
            EventBus.getDefault().post(notifyDataEvent.setData(data)
                    .setBluetoothLeDevice(bluetoothLeDevice)
                    .setBluetoothGattChannel(bluetoothGattInfo));
        }

        @Override
        public void onFailure(BleException exception) {
            if (exception == null) {
                return;
            }
            LogUtils.i("BTManager", "notify fail:" + exception.getDescription());
        }
    };

    /**
     * 操作数据回调
     */
    private IBleCallback bleCallback = new IBleCallback() {
        @Override
        public void onSuccess(final byte[] data, BluetoothGattChannel bluetoothGattInfo, BluetoothLeDevice bluetoothLeDevice) {
            if (data == null) {
                return;
            }
            LogUtils.i("BTManager", "callback success:" + HexUtil.encodeHexStr(data));
            EventBus.getDefault().post(callbackDataEvent.setData(data).setSuccess(true)
                    .setBluetoothLeDevice(bluetoothLeDevice)
                    .setBluetoothGattChannel(bluetoothGattInfo));
            if (bluetoothGattInfo != null && (bluetoothGattInfo.getPropertyType() == PropertyType.PROPERTY_INDICATE
                    || bluetoothGattInfo.getPropertyType() == PropertyType.PROPERTY_NOTIFY)) {
                DeviceMirror deviceMirror = mDeviceMirrorPool.getDeviceMirror(bluetoothLeDevice);
                if (deviceMirror != null) {
                    deviceMirror.setNotifyListener(bluetoothGattInfo.getGattInfoKey(), receiveCallback);
                }
            }
        }

        @Override
        public void onFailure(BleException exception) {
            if (exception == null) {
                return;
            }
            LogUtils.i("BTManager", "callback fail:" + exception.getDescription());
            EventBus.getDefault().post(callbackDataEvent.setSuccess(false));
        }
    };

    private BTManager() {

    }

    public static BTManager getInstance() {
        if (instance == null) {
            synchronized (BTManager.class) {
                if (instance == null) {
                    instance = new BTManager();
                }
            }
        }
        return instance;
    }

    public void init() {
        //蓝牙相关配置修改
        Bluetooth.config()
                .setScanTimeout(15 * 1000)//扫描超时时间，这里设置为永久扫描
                .setScanRepeatInterval(5 * 1000)//扫描间隔5秒
                .setConnectTimeout(10 * 1000)//连接超时时间
                .setOperateTimeout(50 * 1000)//设置数据操作超时时间
                .setConnectRetryCount(3)//设置连接失败重试次数
                .setConnectRetryInterval(1000)//设置连接失败重试间隔时间
                .setOperateRetryCount(3)//设置数据操作失败重试次数
                .setOperateRetryInterval(1000)//设置数据操作失败重试间隔时间
                .setMaxConnectCount(3);//设置最大连接设备数量
        //蓝牙信息初始化，全局唯一，必须在应用初始化时调用
        Bluetooth.getInstance().init();
        mDeviceMirrorPool = Bluetooth.getInstance().getDeviceMirrorPool();
    }

    public void search() {
        Bluetooth.getInstance().startScan(scanCallback);
    }

    public void stopSearch() {
        Bluetooth.getInstance().stopScan(scanCallback);
    }

    public void connect(BluetoothLeDevice bluetoothLeDevice) {
        Bluetooth.getInstance().connect(bluetoothLeDevice, connectCallback);
    }

    public void disconnect(BluetoothLeDevice bluetoothLeDevice) {
        Bluetooth.getInstance().disconnect(bluetoothLeDevice);
    }

    public boolean isConnected(BluetoothLeDevice bluetoothLeDevice) {
        return Bluetooth.getInstance().isConnect(bluetoothLeDevice);
    }

    public void bindChannel(BluetoothLeDevice bluetoothLeDevice, PropertyType propertyType, UUID serviceUUID,
                            UUID characteristicUUID, UUID descriptorUUID) {
        DeviceMirror deviceMirror = mDeviceMirrorPool.getDeviceMirror(bluetoothLeDevice);
        if (deviceMirror != null) {
            BluetoothGattChannel bluetoothGattChannel = new BluetoothGattChannel.Builder()
                    .setBluetoothGatt(deviceMirror.getBluetoothGatt())
                    .setPropertyType(propertyType)
                    .setServiceUUID(serviceUUID)
                    .setCharacteristicUUID(characteristicUUID)
                    .setDescriptorUUID(descriptorUUID)
                    .builder();
            deviceMirror.bindChannel(bleCallback, bluetoothGattChannel);
        }
    }

    public void write(final BluetoothLeDevice bluetoothLeDevice, byte[] data) {
        if (dataInfoQueue != null) {
            dataInfoQueue.clear();
            dataInfoQueue = splitPacketFor20Byte(data);
            LogUtils.i("BTManager", Arrays.toString(data));
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    send(bluetoothLeDevice);
                }
            });
        }
    }

    public void read(BluetoothLeDevice bluetoothLeDevice) {
        DeviceMirror deviceMirror = mDeviceMirrorPool.getDeviceMirror(bluetoothLeDevice);
        if (deviceMirror != null) {
            deviceMirror.readData();
        }
    }

    public void registerNotify(BluetoothLeDevice bluetoothLeDevice, boolean isIndicate) {
        DeviceMirror deviceMirror = mDeviceMirrorPool.getDeviceMirror(bluetoothLeDevice);
        if (deviceMirror != null) {
            deviceMirror.registerNotify(isIndicate);
        }
    }

    //发送队列，提供一种简单的处理方式，实际项目场景需要根据需求优化
    private Queue<byte[]> dataInfoQueue = new LinkedList<>();

    private void send(final BluetoothLeDevice bluetoothLeDevice) {
        if (dataInfoQueue != null && !dataInfoQueue.isEmpty()) {
            DeviceMirror deviceMirror = mDeviceMirrorPool.getDeviceMirror(bluetoothLeDevice);
            if (dataInfoQueue.peek() != null && deviceMirror != null) {
                byte[] data = dataInfoQueue.poll();
                LogUtils.i("BTManager:send", Arrays.toString(data));
                deviceMirror.writeData(data);
            }
            if (dataInfoQueue.peek() != null) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        send(bluetoothLeDevice);
                    }
                }, 100);
            }
        }
    }

    /**
     * 数据分包
     *
     * @param data
     * @return
     */
    private Queue<byte[]> splitPacketFor20Byte(byte[] data) {
        Queue<byte[]> dataInfoQueue = new LinkedList<>();
        if (data != null) {
            int index = 0;
            do {
                byte[] surplusData = new byte[data.length - index];
                byte[] currentData;
                System.arraycopy(data, index, surplusData, 0, data.length - index);
                if (surplusData.length <= 20) {
                    currentData = new byte[surplusData.length];
                    System.arraycopy(surplusData, 0, currentData, 0, surplusData.length);
                    index += surplusData.length;
                } else {
                    currentData = new byte[20];
                    System.arraycopy(data, index, currentData, 0, 20);
                    index += 20;
                }
                dataInfoQueue.offer(currentData);
            } while (index < data.length);
        }
        return dataInfoQueue;
    }

    public void customSearch() {
        Bluetooth.getInstance().getBluetoothAdapter().startDiscovery();
    }

    public void customStopSearch() {
        Bluetooth.getInstance().getBluetoothAdapter().cancelDiscovery();
    }

    private BroadcastReceiver broadcastReceiver;

    public void initBroadcast(Context context) {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    if (device == null) return;
                    BluetoothLeDevice bluetoothLeDevice = new BluetoothLeDevice(device, 0, new byte[]{}, 1);
                    MyApplication.getHandler().post(() -> {
                        scanEvent.getBluetoothLeDeviceStore().addDevice(bluetoothLeDevice);
                        EventBus.getDefault().post(scanEvent.setScanFinish(false).setScanTimeout(false));
                    });
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    EventBus.getDefault().post(scanEvent.setScanFinish(true).setScanTimeout(false));
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public void uninitBroadcast(Context context) {
        if (broadcastReceiver != null)
            context.unregisterReceiver(broadcastReceiver);
    }
}
