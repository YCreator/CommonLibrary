package com.jcx.hnn.debug.bt;


/**
 * Created by yzd on 2018/6/21 0021.
 */

public class IOCommand {

    private IO io = new IO();

    public void setIO(IO io) {
        this.io = io;
    }

    public void sendSystemTime(int year, int month, int day, int hour, int minute, int second) {
        byte[] data = new byte[7];
        data[0] = (byte) ((year >> 8) & 0xff);
        data[1] = (byte) (year & 0xff);
        data[2] = (byte) (month & 0xff);
        data[3] = (byte) (day & 0xff);
        data[4] = (byte) (hour & 0xff);
        data[5] = (byte) (minute & 0xff);
        data[6] = (byte) (second & 0xff);
        sendData((byte) 0x01, data, data.length);
    }

    public void sendPrintheah(int f1, int f2, byte[] dataTemp) {
        byte[] data = new byte[dataTemp.length + 1];
        byte f = (byte) ((f1 << 4) & 0xff);
        byte ff = (byte) (f ^ (f2 & 0xff));
        data[0] = ff;
        System.arraycopy(dataTemp, 0, data, 1, dataTemp.length);
        sendData((byte) 0x02, data, data.length);
    }

    public void sendPayee(byte[] name, byte[] password) {
        byte[] data = new byte[name.length + password.length];
        System.arraycopy(name, 0, data, 0, name.length);
        System.arraycopy(password, 0, data, name.length, password.length);
        sendData((byte) 0x03, data, data.length);
    }

    public void sendSales(byte[] name) {
        byte[] data = new byte[name.length];
        System.arraycopy(name, 0, data, 0, name.length);
        sendData((byte) 0x04, data, data.length);
    }

    public void sendFunc(int x, int y, int z, int h) {
        byte[] data = new byte[2];
        byte b1 = (byte) ((x << 4) & 0xff);
        byte bb1 = (byte) (b1 ^ (y & 0xff));
        data[0] = bb1;
        byte b2 = (byte) ((z << 4) & 0xff);
        byte bb2 = (byte) (b2 ^ (h & 0xff));
        data[1] = bb2;
        sendData((byte) 0x05, data, data.length);
    }

    public void sendTable(byte[] name) {
        byte[] data = new byte[name.length];
        System.arraycopy(name, 0, data, 0, name.length);
        sendData((byte) 0x06, data, data.length);
    }

    public void sendWindow(byte[] name) {
        byte[] data = new byte[name.length];
        System.arraycopy(name, 0, data, 0, name.length);
        sendData((byte) 0x07, data, data.length);
    }

    public void sendClent(byte[] id, byte[] name) {
        byte[] data = new byte[id.length + name.length];
        System.arraycopy(id, 0, data, 0, id.length);
        System.arraycopy(name, 0, data, id.length, name.length);
        sendData((byte) 0x08, data, data.length);
    }

    public void sendForeign(float rate, byte[] name) {
        byte[] data = new byte[4 + name.length];
        String fstr = String.valueOf(rate);
        String[] s = fstr.split(".");
        int z = Integer.parseInt(s[0]);
        data[0] = (byte) ((z >> 8) & 0xff);
        data[1] = (byte) (z & 0xff);
        if (s.length > 1) {
            String ss = s[1];
            int i = ss.length() - 4;
            if (i > 0) {
                for (int j = 0; j < i; j++) ss += "0";
            }
            int x = Integer.parseInt(ss);
            data[2] = (byte) ((x >> 8) & 0xff);
            data[3] = (byte) (x & 0xff);
        } else {
            data[2] = 0x00;
            data[3] = 0x00;
        }
        System.arraycopy(name, 0, data, 4, name.length);
        sendData((byte) 0x09, data, data.length);
    }

    public void sendMealset(byte[] data) {
        sendData((byte) 0x0A, data, data.length);
    }

    public void sendDept(int b1, int b2, float price, byte[] name) {
        byte[] data = new byte[5 + name.length];
        byte b = (byte) ((b1 << 4) & 0xff);
        data[0] = (byte) (b ^ (b2 & 0xff));
        String pstr = String.valueOf(price);
        String[] s = pstr.split(".");
        int z = Integer.parseInt(s[0]);
        data[1] = (byte) ((z >> 8) & 0xff);
        data[2] = (byte) (z & 0xff);
        if (s.length > 1) {
            String ss = s[1];
            int i = ss.length() - 4;
            if (i > 0) {
                for (int j = 0; j < i; j++) ss += "0";
            }
            int x = Integer.parseInt(ss);
            data[3] = (byte) ((x >> 8) & 0xff);
            data[4] = (byte) (x & 0xff);
        } else {
            data[3] = 0x00;
            data[4] = 0x00;
        }
        System.arraycopy(name, 0, data, 5, name.length);
        sendData((byte) 0x0b, data, data.length);

    }

    private synchronized void sendData(byte c, byte[] data, int size) {
        byte b;
        int s;
        s = size;
        this.io.Write((byte) 0x55);
        sendCoded(swap(c));
        sendCoded(c);
        sendCoded((byte) ((s >> 8) & 0xff));
        sendCoded((byte) (s & 0xff));

        for (int i = 0; i < size; i++) {
            b = data[i];
            sendCoded(b);
        }
        int CRC = CRC(data, size);
        sendCoded((byte) (CRC >> 8));
        sendCoded((byte) CRC);
        this.io.Write((byte) 0x5d);
    }

    private byte swap(byte c) {
        byte b = (byte) (c >> 4);
        byte bb = (byte) (c << 4);
        return c < b ? b : bb;
    }

    private void sendCoded(byte b) {
        switch (b) {
            case 0x55: {
                this.io.Write((byte) 0xff);
                this.io.Write((byte) 0xf5);
                break;
            }
            case 0x5d: {
                this.io.Write((byte) 0xff);
                this.io.Write((byte) 0xfd);
                break;
            }
            case (byte) 0xff: {
                this.io.Write((byte) 0xff);
                this.io.Write((byte) 0xf0);
                break;
            }
            default:
                this.io.Write(b);
        }
    }


    private int CRC(byte[] data, int len) {
        int CRC;
        int i, j;
        CRC = 0xffff;
        for (i = 0; i < len; i++) {
            CRC = CRC ^ (data[i] & 0xff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x01) == 1) {
                    CRC = (CRC >> 1) ^ 0xA001;
                } else {
                    CRC = CRC >> 1;
                }
            }
        }

        return CRC;
    }

    public static int[] crc(int[] data) {
        int[] temdata = new int[data.length + 2];
        // unsigned char alen = *aStr – 2; //CRC16只计算前两部分
        int xda, xdapoly;
        int i, j, xdabit;
        xda = 0xFFFF;
        xdapoly = 0xA001; // (X**16 + X**15 + X**2 + 1)
        for (i = 0; i < data.length; i++) {
            xda ^= data[i];
            for (j = 0; j < 8; j++) {
                xdabit = (int) (xda & 0x01);
                xda >>= 1;
                if (xdabit == 1)
                    xda ^= xdapoly;
            }
        }
        System.arraycopy(data, 0, temdata, 0, data.length);
        temdata[temdata.length - 2] = (int) (xda & 0xFF);
        temdata[temdata.length - 1] = (int) (xda >> 8);
        return temdata;
    }

}
