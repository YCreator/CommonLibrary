package com.jcx.hnn.debug.bt;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by yzd on 2018/6/21 0021.
 */

public class BleIO extends IO {

    private final String TAG = "BleIO";

    private final DataInputStream mIn;
    private final DataOutputStream mOut;
    private final BluetoothSocket mSocket;
    private boolean isOpened = false;

    public BleIO(BluetoothSocket socket) {
        this.mSocket = socket;
        DataInputStream tmpIn = null;
        DataOutputStream tmpOut = null;

        // Get the BluetoothSocket input and output streams
        try {
            tmpIn = new DataInputStream(socket.getInputStream());
            tmpOut = new DataOutputStream(socket.getOutputStream());
            isOpened = true;
        } catch (IOException e) {
            Log.e(TAG, "temp sockets not created", e);
        }
        this.mIn = tmpIn;
        this.mOut = tmpOut;
    }

    @Override
    public int Write(byte buffer) {
        try {
            this.mOut.write(buffer);
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int Write(byte[] buffer, int offset, int count) {
        try {
            this.mOut.write(buffer, offset, count);
            this.mOut.flush();
            return count;
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
        }
        return -1;
    }

    @Override
    public int Read(byte[] buffer, int offset, int count, int timeout) {
        int cnt = 0;
        boolean error = false;
        if (this.mIn != null) {
            try {
                long time = System.currentTimeMillis();

                while (System.currentTimeMillis() - time < (long) timeout && cnt != count) {
                    int available = this.mIn.available();
                    if (available > 0) {
                        int rec = this.mIn.read(buffer, offset + cnt, 1);
                        if (rec <= 0) {
                            error = true;
                            break;
                        }

                        Log.i("BTPrinting", "" + buffer[offset + cnt]);
                        cnt += rec;
                    } else {
                        Thread.sleep(10L);
                    }
                }
            } catch (Exception var11) {
                var11.printStackTrace();
                error = true;
            }
        }

        return error && cnt == 0 ? -1 : cnt;
    }

    @Override
    public void Close() {
        try {
            if (this.mSocket != null) {
                this.mSocket.close();
            }
            if (this.mOut != null) {
                this.mOut.close();
            }
            if (this.mIn != null) {
                this.mIn.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean IsOpened() {
        return this.isOpened;
    }

}
