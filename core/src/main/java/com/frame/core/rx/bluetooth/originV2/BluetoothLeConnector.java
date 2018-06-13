package com.frame.core.rx.bluetooth.originV2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.functions.Consumer;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 * <p/>
 * Created by Billin on 2017/5/12.
 */
public class BluetoothLeConnector {

    /**
     * 连接状态回调
     */
    public interface OnConnectListener {
        void onConnect();

        void onDisconnect();

        void onServiceDiscover();

        void onError(String msg);
    }

    /**
     * 读写回调接口
     */
    public interface OnDataAvailableListener {
        void onCharacteristicRead(byte[] values, int status);

        void onCharacteristicChange(UUID characteristic, byte[] values);

        void onCharacteristicWrite(UUID characteristic, int status);

        void onDescriptorWrite(UUID descriptor, int status);

        void onError(String msg);
    }

    private final static String TAG = "BluetoothLe";

    private static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID
            = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private Context mContext;

    private BluetoothAdapter mBluetoothAdapter;

    private String mBluetoothDeviceAddress;

    private Handler mWorkHandler;

    private Handler mAlertHandler;

    private BluetoothGatt mBluetoothGatt;

    private OnConnectListener mOnConnectListener;

    private OnDataAvailableListener mOnDataAvailableListener;

    private AtomicInteger mConnectStatus = new AtomicInteger(BluetoothGatt.STATE_DISCONNECTED);

    private AtomicBoolean mIsStartService = new AtomicBoolean(false);

    private AtomicLong mDisconnectTime = new AtomicLong(SystemClock.elapsedRealtime());

    private AtomicLong mConnectTime = new AtomicLong(SystemClock.elapsedRealtime());

    private BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }

    private void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.mBluetoothGatt = bluetoothGatt;
    }

    private void setOnConnectListener(OnConnectListener l) {
        mOnConnectListener = l;
    }

    /**
     * 分别监听连接状态／服务／读取／写入
     */
    public void setOnDataAvailableListener(OnDataAvailableListener l) {
        mOnDataAvailableListener = l;
    }

    private OnConnectListener getOnConnectListener() {
        return mOnConnectListener;
    }

    public OnDataAvailableListener getOnDataAvailableListener() {
        return mOnDataAvailableListener;
    }

    BluetoothLeConnector(Context c, BluetoothAdapter adapter, String mac, Handler worker) {
        mContext = c.getApplicationContext();
        mBluetoothAdapter = adapter;
        mBluetoothDeviceAddress = mac;

        mWorkHandler = worker;

        HandlerThread thread = new HandlerThread("bluetooth alerter");
        thread.start();
        mAlertHandler = new Handler(thread.getLooper());
    }

    /**
     * Implements callback methods for GATT events that the app cares about. For
     * example, connection change and services discovered.
     * GATT连接的各种监听回调方法
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        /**
         * GATT客户端连接或断开到远程的时候
         * @param gatt      GATT客户端
         * @param status    连接或者断开操作时的状态 {BluetoothGatt#GATT_SUCCESS} 如果操作成功
         * @param newState  返回新的连接状态 {BluetoothProfile#STATE_DISCONNECTED}或者{BluetoothProfile#STATE_CONNECTED}
         */
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status,
                                            final int newState) {
            mWorkHandler.post(() -> {
                Log.d(TAG, "onConnectionStateChange: thread "
                        + Thread.currentThread() + " status " + newState);

                // 清空连接初始化的超时连接任务代码
                mAlertHandler.removeCallbacksAndMessages(null);

                if (status != BluetoothGatt.GATT_SUCCESS) {
                    String err = "Cannot connect device with error status: " + status;
                    disconnectGatt();
                    Log.e(TAG, err);
                    mOnConnectListener.onError(err);
                    mConnectStatus.set(BluetoothGatt.STATE_DISCONNECTED);
                    return;
                }

                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    // setting connect status is connected
                    mConnectStatus.set(BluetoothGatt.STATE_CONNECTED);
                    mOnConnectListener.onConnect();

                    // Attempts to discover services after successful connection.
                    mIsStartService.set(false);
                    if (!gatt.discoverServices()) {
                        String err = "discover service return false";
                        Log.e(TAG, err);
                        gatt.disconnect();
                        mOnConnectListener.onError(err);
                        return;
                    }

                    // 解决连接 Service 过长的问题
                    // 有些手机第一次启动服务的时间大于 2s
                    mAlertHandler.postDelayed(() ->
                            mWorkHandler.post(() -> {
                                if (!mIsStartService.get()) {
                                    gatt.disconnect();
                                }
                            }), 3000L);

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

                    if (!mIsStartService.get()) {
                        String err = "service not found force disconnect";
                        Log.e(TAG, err);
                        mOnConnectListener.onError(err);
                    }

                    mOnConnectListener.onDisconnect();
                    close();
                    mConnectStatus.set(BluetoothGatt.STATE_DISCONNECTED);
                }
            });
        }

        /**
         * 远程服务列表时调用的回调函数，远程设备的 characteristics 和 descriptors 已经更新，已经发现了新服务。
         * @param gatt      调用了{BluetoothGatt#discoverServices}的GATT客户端
         * @param status    如果远程设备已经成功探索，状态为{BluetoothGatt#GATT_SUCCESS}
         */
        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            mWorkHandler.post(() -> {
                // 清空连接服务设置的超时回调
                mIsStartService.set(true);
                mAlertHandler.removeCallbacksAndMessages(null);

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d(TAG, "进入通道连接！！！！ in thread " + Thread.currentThread());
                    mOnConnectListener.onServiceDiscover();
                } else {
                    String err = "onServicesDiscovered received: " + status;
                    Log.e(TAG, err);
                    gatt.disconnect();
                }
            });
        }

        /**
         * 报告一个 characteristic 读取操作的结果
         * @param gatt              调用了{BluetoothGatt#readCharacteristic}的GATT客户端
         * @param characteristic    从关联的远程设备读取的 Characteristic
         * @param status            如果读取操作成功完成，状态为{BluetoothGatt#GATT_SUCCESS}
         */
        @Override
        public void onCharacteristicRead(final BluetoothGatt gatt,
                                         final BluetoothGattCharacteristic characteristic,
                                         final int status) {

            Log.d(TAG, "callback characteristic read status " + status
                    + " in thread " + Thread.currentThread());
            if (status == BluetoothGatt.GATT_SUCCESS && mOnDataAvailableListener != null) {
                mOnDataAvailableListener.onCharacteristicRead(
                        characteristic.getValue(),
                        status);
            }

        }

        /**
         * 由于远程 characteristic 通知而触发的回调
         * @param gatt              GATT client the characteristic is associated with
         * @param characteristic    由于远程通知事件而更新的 Characteristic
         */
        @Override
        public void onCharacteristicChanged(final BluetoothGatt gatt,
                                            final BluetoothGattCharacteristic characteristic) {

            Log.d(TAG, "callback characteristic change in thread " + Thread.currentThread());
            if (mOnDataAvailableListener != null) {
                mOnDataAvailableListener.onCharacteristicChange(
                        characteristic.getUuid(), characteristic.getValue());
            }

        }

        /**
         * characteristic写入操作结果的回调.
         * @param gatt              调用了{BluetoothGatt#writeCharacteristic}的GATT客户端
         * @param characteristic    写入关联的远程设备的 Characteristic
         * @param status            写入操作的结果，如果操作成功，状态为{BluetoothGatt#GATT_SUCCESS}
         */
        @Override
        public void onCharacteristicWrite(final BluetoothGatt gatt,
                                          final BluetoothGattCharacteristic characteristic,
                                          final int status) {
            Log.d(TAG, "callback characteristic write in thread " + Thread.currentThread());
            if (mOnDataAvailableListener != null) {
                mOnDataAvailableListener.onCharacteristicWrite(
                        characteristic.getUuid(), status);
            }
        }

        @Override
        public void onDescriptorWrite(final BluetoothGatt gatt,
                                      final BluetoothGattDescriptor descriptor,
                                      final int status) {
            Log.d(TAG, "callback descriptor write in thread " + Thread.currentThread());

            if (mOnDataAvailableListener != null) {
                mOnDataAvailableListener.onDescriptorWrite(
                        descriptor.getUuid(), status);
            }
        }
    };

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     */
    public void connect(final OnConnectListener callback) {
        mWorkHandler.post(() -> {
            Log.d(TAG, "connect: in thread " + Thread.currentThread());

            if (mBluetoothAdapter == null) {
                String err = "BluetoothAdapter not initialized or unspecified address.";
                Log.e(TAG, err);
                callback.onError(err);
                return;
            }

            final BluetoothDevice device
                    = mBluetoothAdapter.getRemoteDevice(mBluetoothDeviceAddress);
            if (device == null) {
                String err = "Device not found. Unable to connect.";
                Log.e(TAG, err);
                callback.onError(err);
                return;
            }

            // 避免自动硬件断开后又自动连接，导致 service 回调被调用
            // 这里有隐患，实践证明 close 方法是异步调用的且单例，
            // 这就是说当一个 gatt 被创建之后，调用之前的 gatt 可能会把当前的 gatt close掉.
            // 最终造成 gatt 泄漏问题.
            // 一个解决方案就是延长连接硬件的时间
            if (mConnectStatus.get() != BluetoothGatt.STATE_DISCONNECTED) {
                String err = "Device is connecting";
                Log.e(TAG, err);
                callback.onError(err);
                return;
            }

            // 检查完没有任何错误再设置回调，确保上一次没有完成的操作得以继续回调，而不是被新的回调覆盖
            setOnConnectListener(callback);

            // We want to directly connect to the device, so we are setting the
            // autoConnect
            // parameter to false.
            Log.d(TAG, "Trying to create a new connection.");
            mConnectTime.set(SystemClock.elapsedRealtime());
            setBluetoothGatt(device.connectGatt(mContext, false, mGattCallback));
            if (getBluetoothGatt() == null) {
                String err = "bluetooth is not open!";
                Log.e(TAG, err);
                callback.onError(err);
                return;
            }

            mConnectStatus.set(BluetoothGatt.STATE_CONNECTING);
            mIsStartService.set(false);

            // 开一个定时器，如果超出 20s 就强制断开连接
            // 这个定时器必须在连接上设备之后清掉
            mAlertHandler.removeCallbacksAndMessages(null);
            mAlertHandler.postDelayed(() ->
                            mWorkHandler.post(() -> {
                                if (mConnectStatus.get() == BluetoothGatt.STATE_CONNECTING) {
                                    disconnectGatt();
                                    String err = "connect timeout, cannot not connect device";
                                    Log.e(TAG, err);
                                    callback.onError(err);
                                }
                            })
                    , 20000L);
        });
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@link BluetoothGattCallback#onConnectionStateChange(BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        mWorkHandler.post(this::disconnectGatt);
    }

    private void disconnectGatt() {
        Log.d(TAG, "disconnect: in thread " + Thread.currentThread());

        if (mBluetoothAdapter == null || getBluetoothGatt() == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }

        if (mConnectStatus.get() == BluetoothGatt.STATE_DISCONNECTED) {
            close();
            return;
        }

        getBluetoothGatt().disconnect();

        // 确保 Gatt 一定会被 close
        if (mConnectStatus.get() == BluetoothGatt.STATE_CONNECTING) {
            mAlertHandler.removeCallbacksAndMessages(null);
            close();
        }
    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    private void close() {
        Log.d(TAG, "close: in thread " + Thread.currentThread());

        if (getBluetoothGatt() == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mDisconnectTime.set(SystemClock.elapsedRealtime());
        getBluetoothGatt().close();
        setBluetoothGatt(null);
        mConnectStatus.set(BluetoothGatt.STATE_DISCONNECTED);
    }

    private void callDataAvailableListenerError(String err) {
        Log.e(TAG, err);
        if (mOnDataAvailableListener != null) {
            mOnDataAvailableListener.onError(err);
        }
    }

    private void checkChannelAndDo(UUID service,
                                   UUID characteristic,
                                   Consumer<BluetoothGattCharacteristic> action) {

        if (mBluetoothAdapter == null || getBluetoothGatt() == null
                || mConnectStatus.get() != BluetoothGatt.STATE_CONNECTED) {
            callDataAvailableListenerError("should be connect first!");
            return;
        }

        BluetoothGattService serviceChanel = getBluetoothGatt().getService(service);
        if (serviceChanel == null) {
            callDataAvailableListenerError("service is null");
            return;
        }

        BluetoothGattCharacteristic gattCharacteristic
                = serviceChanel.getCharacteristic(characteristic);

        if (characteristic == null) {
            callDataAvailableListenerError("characteristic is null");
            return;
        }

        try {
            action.accept(gattCharacteristic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从蓝牙模块读取数据, 读取的数据将会异步回调到
     * {@link BluetoothLeConnector#setOnDataAvailableListener(OnDataAvailableListener)}
     * 方法设置的监听中
     */
    public void readCharacteristic(final UUID service, final UUID characteristic) {
        mWorkHandler.post(() -> {
            Log.d(TAG, "in readCharacteristic");
            checkChannelAndDo(service, characteristic,
                    bluetoothGattCharacteristic -> {
                        if (getBluetoothGatt()
                                .readCharacteristic(bluetoothGattCharacteristic)) {

                            callDataAvailableListenerError("cannot start characteristic read");
                        }
                    });
        });
    }

    /**
     * write something data to characteristic
     */
    public void writeCharacteristic(final UUID service,
                                    final UUID characteristic,
                                    final byte[] values) {
        mWorkHandler.post(() -> {
            Log.d(TAG, "writing characteristic in thread " + Thread.currentThread());

            checkChannelAndDo(service, characteristic,
                    bluetoothGattCharacteristic -> {
                        bluetoothGattCharacteristic.setValue(values);

                        if (!getBluetoothGatt()
                                .writeCharacteristic(bluetoothGattCharacteristic)) {

                            callDataAvailableListenerError("cannot start characteristic write");
                        }
                    });
        });
    }

    /**
     * 往特定的通道写入数据
     */
    public void writeCharacteristic(UUID service, UUID characteristic, String values) {
        writeCharacteristic(service, characteristic, values.getBytes());
    }

    /**
     * 设置获取特征值UUID通知
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification. False otherwise.
     */
    public void setCharacteristicNotification(final UUID service,
                                              final UUID characteristic,
                                              final boolean enabled) {

        mWorkHandler.post(() ->
                checkChannelAndDo(service, characteristic, gattCharacteristic -> {
                    if (enabled) {
                        Log.i(TAG, "Enable Notification");
                        getBluetoothGatt().setCharacteristicNotification(gattCharacteristic, true);
                        BluetoothGattDescriptor descriptor = gattCharacteristic
                                .getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

                        if (!getBluetoothGatt().writeDescriptor(descriptor)) {
                            callDataAvailableListenerError("cannot open notification channel");
                        }
                    } else {
                        Log.i(TAG, "Disable Notification");
                        getBluetoothGatt().setCharacteristicNotification(gattCharacteristic, false);
                        BluetoothGattDescriptor descriptor = gattCharacteristic
                                .getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
                        descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);

                        if (!getBluetoothGatt().writeDescriptor(descriptor)) {
                            callDataAvailableListenerError("cannot close notification channel");
                        }
                    }
                })
        );
    }
}
