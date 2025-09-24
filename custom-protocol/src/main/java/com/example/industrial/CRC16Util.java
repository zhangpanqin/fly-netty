package com.example.industrial;

public class CRC16Util {
    public static int crc16(byte[] data, int offset, int length) {
        int crc = 0xFFFF;           // 初始值
        for (int i = offset; i < offset + length; i++) {
            crc ^= data[i] & 0xFF;
            for (int j = 0; j < 8; j++) {
                if ((crc & 1) != 0) {
                    crc >>>= 1;
                    crc ^= 0xA001;  // 0x8005 的反射多项式
                } else {
                    crc >>>= 1;
                }
            }
        }
        return crc;
    }
}