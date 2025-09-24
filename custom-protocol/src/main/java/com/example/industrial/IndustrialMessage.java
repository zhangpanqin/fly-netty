package com.example.industrial;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class IndustrialMessage {
    private byte start = (byte) 0xAA; // 固定起始符
    private byte deviceId;
    private byte command;
    private ByteBuf data;

    public IndustrialMessage() {
    }

    public IndustrialMessage(byte deviceId, byte command, ByteBuf data) {
        this.deviceId = deviceId;
        this.command = command;
        this.data = data;
    }

    // Getters and Setters
    public byte getStart() {
        return start;
    }

    public byte getDeviceId() {
        return deviceId;
    }

    public byte getCommand() {
        return command;
    }

    public ByteBuf getData() {
        return data;
    }

    public void setDeviceId(byte deviceId) {
        this.deviceId = deviceId;
    }

    public void setCommand(byte command) {
        this.command = command;
    }

    public void setData(ByteBuf data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IndustrialMessage{");
        sb.append("start=0x").append(Integer.toHexString(start & 0xFF));
        sb.append(", deviceId=0x").append(Integer.toHexString(deviceId & 0xFF));
        sb.append(", command=0x").append(Integer.toHexString(command & 0xFF));
        if (data != null) {
            sb.append(", dataLength=").append(data.readableBytes());
            sb.append(", data=");
            for (int i = 0; i < data.readableBytes(); i++) {
                sb.append(Integer.toHexString(data.getByte(i) & 0xFF)).append(" ");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public void setStart(byte start) {
        this.start = start;
    }
}