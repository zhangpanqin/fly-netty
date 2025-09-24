package com.example.industrial;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class IndustrialProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 最小帧：起始符(1)+ID(1)+命令(1)+长度(2)+数据(N)+CRC(2)
        if (in.readableBytes() < 7) {
            return;
        }

        in.markReaderIndex();

        // 读取起始符
        byte start = in.readByte();
        if (start != (byte) 0xAA) {
            in.resetReaderIndex();
            in.readByte(); // 跳过无效字节
            return;
        }

        byte deviceId = in.readByte();
        byte command = in.readByte();
        int dataLength = in.readUnsignedShort(); // 大端

        if (dataLength < 0 || dataLength > 1024) { // 防止异常长度
            ctx.close();
            return;
        }

        if (in.readableBytes() < dataLength + 2) { // 数据 + CRC
            in.resetReaderIndex();
            return;
        }

        ByteBuf data = in.readRetainedSlice(dataLength);


        // 将当前帧（不含起始符）复制到字节数组用于 CRC 校验
        byte[] frame = new byte[1 + 1 + 2 + dataLength]; // deviceId + command + length + data
        frame[0] = deviceId;
        frame[1] = command;
        frame[2] = (byte) (dataLength >> 8);        // 高位
        frame[3] = (byte) (dataLength & 0xFF);      // 低位
        data.getBytes(0, frame, 4, dataLength);

        // 读取数据域后，准备验证 CRC
        byte[] crcData = new byte[1 + 1 + 2 + dataLength]; // deviceId + command + length + data

// 手动复制参与 CRC 计算的字节
        crcData[0] = deviceId;
        crcData[1] = command;
        crcData[2] = (byte) (dataLength >> 8);        // 高位
        crcData[3] = (byte) (dataLength & 0xFF);      // 低位
        data.getBytes(0, crcData, 4, dataLength);

// 计算 CRC
        int calculatedCrc = CRC16Util.crc16(crcData, 0, crcData.length);

// 读取接收到的 CRC（低字节在前 → 小端）
        int receivedCrcLow = in.readUnsignedByte();
        int receivedCrcHigh = in.readUnsignedByte();
        int receivedCrc = (receivedCrcHigh << 8) | receivedCrcLow; // 组合成大端值

// 比较
        if (receivedCrc != calculatedCrc) {
            data.release();
            throw new IllegalArgumentException(
                    String.format("CRC校验失败，期望=0x%04X, 实际=0x%04X", calculatedCrc, receivedCrc)
            );
        }

        IndustrialMessage message = new IndustrialMessage(deviceId, command, data);
        out.add(message);
    }
}