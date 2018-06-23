package com.jcx.hnn.debug.bt;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.frame.core.util.utils.LogUtils;

import java.util.Arrays;

/**
 * Created by yzd on 2018/6/21 0021.
 */

public class BleUtil {

    private BluetoothService chatService;
    private Callback callback;
    private IOCommand command;
    private String mConnectedDeviceName;
    private static BleUtil ble;

    private BleUtil() {

    }

    public static BleUtil instance() {
        if (ble == null) {
            ble = new BleUtil();
        }
        return ble;
    }

    public BluetoothService setup(Callback callback) {
        this.callback = callback;
        if (chatService == null)
            chatService = new BluetoothService(mHandler);
        return chatService;
    }

    public BleUtil setCommand(IOCommand command) {
        this.command = command;
        return this;
    }

    public String getConnectedDeviceName() {
        return mConnectedDeviceName;
    }

    public void onStart() {
        if (chatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (chatService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                chatService.start();
            }
        }
    }

    public void onStop() {
        if (chatService != null) {
            chatService.stop();
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.Constants.MESSAGE_STATE_CHANGE:
                    if (callback != null) {
                        callback.connectStatus(msg.arg1);
                    }
                    break;
                case BluetoothService.Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    if (callback != null) {
                        callback.writeListener(writeMessage);
                    }
                    LogUtils.d("Ble:Write:Me", writeMessage);
                    break;
                case BluetoothService.Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    if (callback != null) {
                        callback.readListener("byte:" + Arrays.toString(readBuf) + "\n" + "string:" + readMessage);
                    }
                    LogUtils.d("Ble:Read:" + mConnectedDeviceName, readMessage);
                    break;
                case BluetoothService.Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothService.Constants.DEVICE_NAME);
                    if (command != null) {
                        command.setIO(chatService.getIO());
                    }
                    LogUtils.d("Ble:DeviceName", mConnectedDeviceName);
                    break;
                case BluetoothService.Constants.MESSAGE_TOAST:
                    LogUtils.d("Ble:Toast", BluetoothService.Constants.TOAST);
                    break;
            }
        }
    };

    public interface Callback {
        void connectStatus(int status);

        void writeListener(String data);

        void readListener(String data);

    }

}
