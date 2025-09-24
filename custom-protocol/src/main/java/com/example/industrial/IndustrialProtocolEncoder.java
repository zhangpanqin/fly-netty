package com.example.industrial;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class IndustrialProtocolEncoder extends MessageToByteEncoder<IndustrialMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, IndustrialMessage msg, ByteBuf out) throws Exception {
        int dataLength = msg.getData().readableBytes();
        byte[] frame = new byte[1 + 2 + dataLength]; // command + length + data

        frame[0] = msg.getCommand();
        frame[1] = (byte) (dataLength >> 8);
        frame[2] = (byte) (dataLength & 0xFF);
        msg.getData().getBytes(0, frame, 3, dataLength);


        byte[] crcData = new byte[1 + 1 + 2 + dataLength];
        crcData[0] = msg.getDeviceId();
        crcData[1] = msg.getCommand();
        crcData[2] = (byte) (dataLength >> 8);
        crcData[3] = (byte) (dataLength & 0xFF);
        msg.getData().getBytes(0, crcData, 4, dataLength);

        int crc = CRC16Util.crc16(crcData, 0, crcData.length);

        // 发送：起始符 + deviceId + command + length + data + CRC（低字节在前）
        out.writeByte(msg.getStart());
        out.writeByte(msg.getDeviceId());
        out.writeByte(msg.getCommand());
        out.writeShort(dataLength);
        out.writeBytes(msg.getData());

        // ✅ CRC 低字节在前（小端）
        out.writeByte(crc & 0xFF);        // 低字节
        out.writeByte((crc >> 8) & 0xFF); // 高字节
    }
}