package com.jcx.hnn.debug.bt;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.frame.core.adapter.BaseRvAdapter;
import com.frame.core.interf.AdapterItem;
import com.frame.core.rx.bluetooth.BluetoothClient;
import com.frame.core.rx.bluetooth.BluetoothClientBLEV2Adapter;
import com.frame.core.rx.bluetooth.bean.BLEDevice;
import com.frame.core.rx.bluetooth.callback.BaseResultCallback;
import com.frame.core.rx.bluetooth.originV2.BluetoothLeInitialization;
import com.frame.core.util.utils.StringUtils;
import com.frame.core.util.utils.ToastUtils;
import com.lib.commonlibrary.MyApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by yzd on 2018/6/14 0014.
 */

public final class BTHelper {

    private static BTHelper mHelper;

    private BluetoothClient client;
    private Search mSearch;
    private String mMac;
    private static final UUID UUID_SERVICE_CHANNEL
            = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");

    private static final UUID UUID_CHARACTERISTIC_CHANNEL
            = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    private BTHelper() {
        init();
    }

    public static BTHelper start() {
        if (mHelper == null) {
            mHelper = new BTHelper();
        }
        return mHelper;
    }


    /**
     * 初始化
     */
    private void init() {
        client = new BluetoothClientBLEV2Adapter(BluetoothLeInitialization.getInstance(MyApplication.get_context()));
        client.openBluetooth();
        mSearch = new Search(client);
    }

    /**
     * 获取蓝牙搜索对象
     *
     * @return
     */
    public Search getSearch() {
        return mSearch;
    }

    /**
     * 蓝牙连接
     *
     * @param mac
     * @param callback
     */
    public void connect(String mac, BaseResultCallback<byte[]> callback) {
        client.stopSearch();
        if (!StringUtils.isEmpty(mac) && !mac.equals(mMac))
            client.connect(mac).subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(String s) {
                    ToastUtils.showShort("连接成功:" + s);
                    mMac = s;
                    if (callback != null)
                        client.registerNotify(mMac, UUID_SERVICE_CHANNEL,
                                UUID_CHARACTERISTIC_CHANNEL, callback).subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String s) {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.showShort("连接失败：" + e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });
    }

    /**
     * 蓝衣发送数据
     *
     * @param mac
     * @param callback
     * @return
     */
    public Write connectAndWrite(String mac, BaseResultCallback<byte[]> callback) {
        if (StringUtils.isEmpty(mMac)) {
            connect(mac, callback);
        } else if (!mac.equals(mMac)) {
            disConnect();
            mMac = null;
            connect(mac, callback);
        }
        return new Write(client, mMac, UUID_SERVICE_CHANNEL, UUID_CHARACTERISTIC_CHANNEL);
    }

    /**
     * 蓝牙断开连接
     */
    public void disConnect() {
        if (!StringUtils.isEmpty(mMac)) {
            client.disconnect(mMac);
            client.unRegisterNotify(mMac, UUID_SERVICE_CHANNEL,
                    UUID_CHARACTERISTIC_CHANNEL);
            mMac = null;
        }

    }

    /**
     * 获取蓝牙客户端
     *
     * @return
     */
    public BluetoothClient getClient() {
        return client;
    }

    public String getCurrentMac() {
        return mMac;
    }

    public static class Write {

        private String mMac;
        private UUID UUID_SERVICE_CHANNEL;

        private UUID UUID_CHARACTERISTIC_CHANNEL;
        private BluetoothClient client;

        private Write(BluetoothClient client, String mac, UUID UUID_SERVICE_CHANNEL, UUID UUID_CHARACTERISTIC_CHANNEL) {
            this.mMac = mac;
            this.client = client;
            this.UUID_CHARACTERISTIC_CHANNEL = UUID_CHARACTERISTIC_CHANNEL;
            this.UUID_SERVICE_CHANNEL = UUID_SERVICE_CHANNEL;
        }

        /**
         * 写入发送蓝牙数据
         *
         * @param data
         */
        public void write(byte[] data) {
            if (!StringUtils.isEmpty(mMac)) {
                client.write(mMac, UUID_SERVICE_CHANNEL,
                        UUID_CHARACTERISTIC_CHANNEL, data).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        ToastUtils.showShort("发送数据：" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("发送数据失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }
    }

    public static class Search {
        private List<BLEDevice> list;
        private BaseRvAdapter<BLEDevice> adapter;
        private BluetoothClient client;

        private Search(BluetoothClient client) {
            this.client = client;
            list = new ArrayList<>();
        }

        /**
         * 使用recyclerView展示蓝牙设备列表
         *
         * @param recyclerView
         * @param item
         * @return
         */
        public Search setAdapterItem(RecyclerView recyclerView, AdapterItem<BLEDevice> item) {
            adapter = new BaseRvAdapter<BLEDevice>(list) {
                @NonNull
                @Override
                public AdapterItem<BLEDevice> onCreateItem(int viewType) {
                    return item;
                }
            };
            recyclerView.setAdapter(adapter);

            return this;
        }

        /**
         * 获取蓝牙设备数组
         *
         * @return
         */
        public List<BLEDevice> getDevices() {
            return list;
        }

        /**
         * 停止搜索
         */
        public void stopSearch() {
            client.stopSearch();
        }

        /**
         * 搜索蓝牙设备
         *
         * @param millis
         * @param cancel
         */
        public void search(int millis, boolean cancel) {
            client.search(millis, cancel).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BLEDevice>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d("BTHelper:onSubscribe", "onSubscribe");
                            if (adapter != null) {
                                adapter.itemsClear();
                            } else {
                                list.clear();
                            }

                        }

                        @Override
                        public void onNext(BLEDevice bleDevice) {
                            Log.d("BTHelper:onNext", bleDevice.toString());
                            if (adapter != null) {
                                adapter.addItem(bleDevice);
                            } else {
                                list.add(bleDevice);
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("BTHelper:onError", "onError");
                        }

                        @Override
                        public void onComplete() {
                            Log.d("BTHelper:onComplete", "onComplete");

                        }
                    });
        }
    }
}
