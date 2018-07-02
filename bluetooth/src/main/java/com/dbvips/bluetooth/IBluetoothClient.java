package com.dbvips.bluetooth;

import com.dbvips.bluetooth.connect.listener.BleConnectStatusListener;
import com.dbvips.bluetooth.connect.options.BleConnectOptions;
import com.dbvips.bluetooth.connect.response.BleConnectResponse;
import com.dbvips.bluetooth.connect.response.BleMtuResponse;
import com.dbvips.bluetooth.connect.response.BleNotifyResponse;
import com.dbvips.bluetooth.connect.response.BleReadResponse;
import com.dbvips.bluetooth.connect.response.BleReadRssiResponse;
import com.dbvips.bluetooth.connect.response.BleUnnotifyResponse;
import com.dbvips.bluetooth.connect.response.BleWriteResponse;
import com.dbvips.bluetooth.receiver.listener.BluetoothBondListener;
import com.dbvips.bluetooth.connect.listener.BluetoothStateListener;
import com.dbvips.bluetooth.search.SearchRequest;
import com.dbvips.bluetooth.search.response.SearchResponse;

import java.util.UUID;

/**
 * 蓝牙客户端接口
 * Created by dingjikerbo on 2016/8/25.
 */
public interface IBluetoothClient {

    /**
     * 蓝牙连接
     *
     * @param mac      蓝牙地址
     * @param options  连接配置
     * @param response 响应监听
     */
    void connect(String mac, BleConnectOptions options, BleConnectResponse response);

    /**
     * 断开连接
     *
     * @param mac 蓝牙地址
     */
    void disconnect(String mac);

    /**
     * 注册蓝牙连接状态监听
     *
     * @param mac      蓝牙地址
     * @param listener 蓝牙连接状态监听
     */
    void registerConnectStatusListener(String mac, BleConnectStatusListener listener);

    /**
     * 注销蓝牙连接状态监听
     *
     * @param mac      蓝牙地址
     * @param listener 蓝牙连接状态监听
     */
    void unregisterConnectStatusListener(String mac, BleConnectStatusListener listener);

    /**
     * 读取数据
     *
     * @param mac       蓝牙地址
     * @param service   蓝牙服务端uuid
     * @param character 特征uuid
     * @param response  响应返回体
     */
    void read(String mac, UUID service, UUID character, BleReadResponse response);

    /**
     * 写入数据
     *
     * @param mac       蓝牙地址
     * @param service   蓝牙服务端uuid
     * @param character 特征uuid
     * @param value     写入数据
     * @param response  写入响应返回体
     */
    void write(String mac, UUID service, UUID character, byte[] value, BleWriteResponse response);

    /**
     * 读取描述符
     *
     * @param mac        蓝牙地址
     * @param service    蓝牙服务端uuid
     * @param character  特征uuid
     * @param descriptor 描述符uuid
     * @param response   响应返回体
     */
    void readDescriptor(String mac, UUID service, UUID character, UUID descriptor, BleReadResponse response);

    /**
     * 写入描述符
     *
     * @param mac        蓝牙地址
     * @param service    蓝牙服务端uuid
     * @param character  特征uuid
     * @param descriptor 描述符uuid
     * @param value      写入数据
     * @param response   响应返回体
     */
    void writeDescriptor(String mac, UUID service, UUID character, UUID descriptor, byte[] value, BleWriteResponse response);

    /**
     * 写入数据无需peripheral回复response的写入请求
     *
     * @param mac       蓝牙地址
     * @param service   蓝牙服务端uuid
     * @param character 特征uuid
     * @param value     写入数据
     * @param response  响应返回体
     */
    void writeNoRsp(String mac, UUID service, UUID character, byte[] value, BleWriteResponse response);

    /**
     * 蓝牙通知
     *
     * @param mac       蓝牙地址
     * @param service   蓝牙服务端uuid
     * @param character 特征uuid
     * @param response  通知响应返回体
     */
    void notify(String mac, UUID service, UUID character, BleNotifyResponse response);

    /**
     * 取消通知
     *
     * @param mac       蓝牙地址
     * @param service   蓝牙服务端uuid
     * @param character 特征uuid
     * @param response  通知响应返回体
     */
    void unnotify(String mac, UUID service, UUID character, BleUnnotifyResponse response);

    /**
     * 指示
     *
     * @param mac       蓝牙地址
     * @param service   蓝牙服务端uuid
     * @param character 特征uuid
     * @param response  通知响应返回体
     */
    void indicate(String mac, UUID service, UUID character, BleNotifyResponse response);

    /**
     * 取消指示
     *
     * @param mac       蓝牙地址
     * @param service   蓝牙服务端uuid
     * @param character 特征uuid
     * @param response  通知响应返回体
     */
    void unindicate(String mac, UUID service, UUID character, BleUnnotifyResponse response);

    /**
     * 读取信号强度指示
     *
     * @param mac      蓝牙地址
     * @param response 信号响应返回体
     */
    void readRssi(String mac, BleReadRssiResponse response);

    /**
     * requestMtu
     *
     * @param mac      蓝牙地址
     * @param mtu      mtu
     * @param response mtu响应返回体
     */
    void requestMtu(String mac, int mtu, BleMtuResponse response);

    /**
     * 蓝牙搜索
     *
     * @param request  搜索请求体
     * @param response 搜索响应体
     */
    void search(SearchRequest request, SearchResponse response);

    /**
     * 停止搜索
     */
    void stopSearch();

    /**
     * 注册蓝牙状态监听
     *
     * @param listener 蓝牙状态监听
     */
    void registerBluetoothStateListener(BluetoothStateListener listener);

    /**
     * 撤销蓝牙状态监听
     *
     * @param listener 蓝牙状态监听
     */
    void unregisterBluetoothStateListener(BluetoothStateListener listener);

    /**
     * 注册蓝牙绑定服务监听
     *
     * @param listener 蓝牙绑定监听
     */
    void registerBluetoothBondListener(BluetoothBondListener listener);

    /**
     * 撤销蓝牙绑定服务监听
     *
     * @param listener 蓝牙绑定监听
     */
    void unregisterBluetoothBondListener(BluetoothBondListener listener);

    /**
     * 清理请求
     *
     * @param mac  蓝牙地址
     * @param type 类型
     */
    void clearRequest(String mac, int type);

    /**
     * 刷新缓存
     *
     * @param mac
     */
    void refreshCache(String mac);
}
