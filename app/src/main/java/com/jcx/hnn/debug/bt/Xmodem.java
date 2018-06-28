package com.jcx.hnn.debug.bt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by yzd on 2018/6/28 0028.
 */

public class Xmodem {

    // 开始
    private final byte SOH = 0x01;
    // 结束
    private final byte EOT = 0x04;
    // 应答
    private final byte ACK = 0x06;
    // 重传
    private final byte NAK = 0x15;
    // 无条件结束
    private final byte CAN = 0x18;

    // 以128字节块的形式传输数据
    private final int SECTOR_SIZE = 128;
    // 最大错误（无应答）包数
    private final int MAX_ERRORS = 10;

    private IO io = new IO();

    public void setIO(IO io) {
        this.io = io;
    }

    public void send(byte[] data) {
        new Thread() {
            @Override
            public void run() {
                try {
                    // 错误包数
                    int errorCount;
                    // 包序号
                    byte blockNumber = 0x01;
                    // 校验和
                    int checkSum;

                    int size = data.length;
                    for (int i = 0, j = i * SECTOR_SIZE; j < size; j = (i++ * SECTOR_SIZE)) {
                        // 初始化数据缓冲区
                        byte[] sector = new byte[SECTOR_SIZE];
                        int x = size - j;
                        if (x < SECTOR_SIZE) {
                            System.arraycopy(data, j, sector, 0, x);
                            for (int y = 0; y < SECTOR_SIZE - x; y++) {
                                sector[x + y] = (byte) 0xff;
                            }
                        } else {
                            System.arraycopy(data, j, sector, 0, SECTOR_SIZE);
                        }
                        // 同一包数据最多发送10次
                        errorCount = 0;
                        while (errorCount < MAX_ERRORS) {
                            // 组包
                            // 控制字符 + 包序号 + 包序号的反码 + 数据 + 校验和
                            putData(SOH);
                            putData(blockNumber);
                            putData(~blockNumber);
                            checkSum = CRC16.calc(sector) & 0x00ffff;
                            putChar(sector, (short) checkSum);
                            io.Flush();

                            // 获取应答数据
                            byte data = getData();
                            // 如果收到应答数据则跳出循环，发送下一包数据
                            // 未收到应答，错误包数+1，继续重发
                            if (data == ACK) {
                                break;
                            } else {
                                ++errorCount;
                            }
                        }
                        // 包序号自增
                        blockNumber = (byte) ((++blockNumber) % 256);
                    }

                    // 所有数据发送完成后，发送结束标识
                    boolean isAck = false;
                    while (!isAck) {
                        putData(EOT);
                        isAck = getData() == ACK;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 发送数据
     *
     * @param filePath 文件路径
     */
    public void send(final String filePath) {
        new Thread() {
            public void run() {
                try {
                    // 错误包数
                    int errorCount;
                    // 包序号
                    byte blockNumber = 0x01;
                    // 校验和
                    int checkSum;
                    // 读取到缓冲区的字节数量
                    int nbytes;
                    // 初始化数据缓冲区
                    byte[] sector = new byte[SECTOR_SIZE];
                    // 读取文件初始化
                    DataInputStream inputStream = new DataInputStream(
                            new FileInputStream(filePath));

                    while ((nbytes = inputStream.read(sector)) > 0) {
                        // 如果最后一包数据小于128个字节，以0xff补齐
                        if (nbytes < SECTOR_SIZE) {
                            for (int i = nbytes; i < SECTOR_SIZE; i++) {
                                sector[i] = (byte) 0xff;
                            }
                        }

                        // 同一包数据最多发送10次
                        errorCount = 0;
                        while (errorCount < MAX_ERRORS) {
                            // 组包
                            // 控制字符 + 包序号 + 包序号的反码 + 数据 + 校验和
                            putData(SOH);
                            putData(blockNumber);
                            putData(~blockNumber);
                            checkSum = CRC16.calc(sector) & 0x00ffff;
                            putChar(sector, (short) checkSum);
                            io.Flush();

                            // 获取应答数据
                            byte data = getData();
                            // 如果收到应答数据则跳出循环，发送下一包数据
                            // 未收到应答，错误包数+1，继续重发
                            if (data == ACK) {
                                break;
                            } else {
                                ++errorCount;
                            }
                        }
                        // 包序号自增
                        blockNumber = (byte) ((++blockNumber) % 256);
                    }

                    // 所有数据发送完成后，发送结束标识
                    boolean isAck = false;
                    while (!isAck) {
                        putData(EOT);
                        isAck = getData() == ACK;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    /**
     * 接收数据
     *
     * @param filePath 文件路径
     * @return 是否接收完成
     * @throws IOException 异常
     */
    public boolean receive(String filePath) throws Exception {
        // 错误包数
        int errorCount = 0;
        // 包序号
        byte blocknumber = 0x01;
        // 数据
        byte data;
        // 校验和
        int checkSum;
        // 初始化数据缓冲区
        byte[] sector = new byte[SECTOR_SIZE];
        // 写入文件初始化
        DataOutputStream outputStream = new DataOutputStream(
                new FileOutputStream(filePath));

        // 发送字符C，CRC方式校验
        putData((byte) 0x43);

        while (true) {
            if (errorCount > MAX_ERRORS) {
                outputStream.close();
                return false;
            }

            // 获取应答数据
            data = getData();
            if (data != EOT) {
                try {
                    // 判断接收到的是否是开始标识
                    if (data != SOH) {
                        errorCount++;
                        continue;
                    }

                    // 获取包序号
                    data = getData();
                    // 判断包序号是否正确
                    if (data != blocknumber) {
                        errorCount++;
                        continue;
                    }

                    // 获取包序号的反码
                    byte _blocknumber = (byte) ~getData();
                    // 判断包序号的反码是否正确
                    if (data != _blocknumber) {
                        errorCount++;
                        continue;
                    }

                    // 获取数据
                    for (int i = 0; i < SECTOR_SIZE; i++) {
                        sector[i] = getData();
                    }

                    // 获取校验和
                    checkSum = (getData() & 0xff) << 8;
                    checkSum |= (getData() & 0xff);
                    // 判断校验和是否正确
                    int crc = CRC16.calc(sector);
                    if (crc != checkSum) {
                        errorCount++;
                        continue;
                    }

                    // 发送应答
                    putData(ACK);
                    // 包序号自增
                    blocknumber++;
                    // 将数据写入本地
                    outputStream.write(sector);
                    // 错误包数归零
                    errorCount = 0;

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    // 如果出错发送重传标识
                    if (errorCount != 0) {
                        putData(NAK);
                    }
                }
            } else {
                break;
            }
        }

        // 关闭输出流
        outputStream.close();
        // 发送应答
        putData(ACK);

        return true;
    }

    /**
     * 获取数据
     *
     * @return 数据
     * @throws IOException 异常
     */
    private byte getData() throws IOException {
        return (byte) io.Read();
    }

    /**
     * 发送数据
     *
     * @param data 数据
     * @throws IOException 异常
     */
    private void putData(int data) throws IOException {
        io.Write((byte) data);
    }

    /**
     * 发送数据
     *
     * @param data     数据
     * @param checkSum 校验和
     * @throws IOException 异常
     */
    private void putChar(byte[] data, short checkSum) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(data.length + 2).order(
                ByteOrder.BIG_ENDIAN);
        bb.put(data);
        bb.putShort(checkSum);
        io.Write(bb.array());
    }

    public static class CRC16 {

        private static final char crctable[] = {0x0000, 0x1021, 0x2042, 0x3063,
                0x4084, 0x50a5, 0x60c6, 0x70e7, 0x8108, 0x9129, 0xa14a, 0xb16b,
                0xc18c, 0xd1ad, 0xe1ce, 0xf1ef, 0x1231, 0x0210, 0x3273, 0x2252,
                0x52b5, 0x4294, 0x72f7, 0x62d6, 0x9339, 0x8318, 0xb37b, 0xa35a,
                0xd3bd, 0xc39c, 0xf3ff, 0xe3de, 0x2462, 0x3443, 0x0420, 0x1401,
                0x64e6, 0x74c7, 0x44a4, 0x5485, 0xa56a, 0xb54b, 0x8528, 0x9509,
                0xe5ee, 0xf5cf, 0xc5ac, 0xd58d, 0x3653, 0x2672, 0x1611, 0x0630,
                0x76d7, 0x66f6, 0x5695, 0x46b4, 0xb75b, 0xa77a, 0x9719, 0x8738,
                0xf7df, 0xe7fe, 0xd79d, 0xc7bc, 0x48c4, 0x58e5, 0x6886, 0x78a7,
                0x0840, 0x1861, 0x2802, 0x3823, 0xc9cc, 0xd9ed, 0xe98e, 0xf9af,
                0x8948, 0x9969, 0xa90a, 0xb92b, 0x5af5, 0x4ad4, 0x7ab7, 0x6a96,
                0x1a71, 0x0a50, 0x3a33, 0x2a12, 0xdbfd, 0xcbdc, 0xfbbf, 0xeb9e,
                0x9b79, 0x8b58, 0xbb3b, 0xab1a, 0x6ca6, 0x7c87, 0x4ce4, 0x5cc5,
                0x2c22, 0x3c03, 0x0c60, 0x1c41, 0xedae, 0xfd8f, 0xcdec, 0xddcd,
                0xad2a, 0xbd0b, 0x8d68, 0x9d49, 0x7e97, 0x6eb6, 0x5ed5, 0x4ef4,
                0x3e13, 0x2e32, 0x1e51, 0x0e70, 0xff9f, 0xefbe, 0xdfdd, 0xcffc,
                0xbf1b, 0xaf3a, 0x9f59, 0x8f78, 0x9188, 0x81a9, 0xb1ca, 0xa1eb,
                0xd10c, 0xc12d, 0xf14e, 0xe16f, 0x1080, 0x00a1, 0x30c2, 0x20e3,
                0x5004, 0x4025, 0x7046, 0x6067, 0x83b9, 0x9398, 0xa3fb, 0xb3da,
                0xc33d, 0xd31c, 0xe37f, 0xf35e, 0x02b1, 0x1290, 0x22f3, 0x32d2,
                0x4235, 0x5214, 0x6277, 0x7256, 0xb5ea, 0xa5cb, 0x95a8, 0x8589,
                0xf56e, 0xe54f, 0xd52c, 0xc50d, 0x34e2, 0x24c3, 0x14a0, 0x0481,
                0x7466, 0x6447, 0x5424, 0x4405, 0xa7db, 0xb7fa, 0x8799, 0x97b8,
                0xe75f, 0xf77e, 0xc71d, 0xd73c, 0x26d3, 0x36f2, 0x0691, 0x16b0,
                0x6657, 0x7676, 0x4615, 0x5634, 0xd94c, 0xc96d, 0xf90e, 0xe92f,
                0x99c8, 0x89e9, 0xb98a, 0xa9ab, 0x5844, 0x4865, 0x7806, 0x6827,
                0x18c0, 0x08e1, 0x3882, 0x28a3, 0xcb7d, 0xdb5c, 0xeb3f, 0xfb1e,
                0x8bf9, 0x9bd8, 0xabbb, 0xbb9a, 0x4a75, 0x5a54, 0x6a37, 0x7a16,
                0x0af1, 0x1ad0, 0x2ab3, 0x3a92, 0xfd2e, 0xed0f, 0xdd6c, 0xcd4d,
                0xbdaa, 0xad8b, 0x9de8, 0x8dc9, 0x7c26, 0x6c07, 0x5c64, 0x4c45,
                0x3ca2, 0x2c83, 0x1ce0, 0x0cc1, 0xef1f, 0xff3e, 0xcf5d, 0xdf7c,
                0xaf9b, 0xbfba, 0x8fd9, 0x9ff8, 0x6e17, 0x7e36, 0x4e55, 0x5e74,
                0x2e93, 0x3eb2, 0x0ed1, 0x1ef0};

        public static char calc(byte[] bytes) {
            char crc = 0x0000;
            for (byte b : bytes) {
                crc = (char) ((crc << 8) ^ crctable[((crc >> 8) ^ b) & 0x00ff]);
            }
            return crc;
        }
    }

}
