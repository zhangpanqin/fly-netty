// ClientHandler.java
package com.example.industrial;

import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends SimpleChannelInboundHandler<IndustrialMessage> {

    private final IndustrialClient client;

    public ClientHandler(IndustrialClient client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IndustrialMessage msg) {
        try {
            byte deviceId = msg.getDeviceId();
            byte command = msg.getCommand();
            io.netty.buffer.ByteBuf data = msg.getData();

            // 提取数据字符串（UTF-8）
            byte[] dataBytes = new byte[data.readableBytes()];
            data.readBytes(dataBytes);
            String dataStr = new String(dataBytes, "UTF-8");

            System.out.println("\n📥 收到服务端响应");
            System.out.println("   设备: " + String.format("%02X", deviceId));
            System.out.println("   命令: " + String.format("%02X", command));
            System.out.println("   数据: " + dataStr);
            System.out.print("\n> "); // 恢复输入提示
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println("❌ 客户端异常: " + cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("\n🔗 与服务端断开连接");
    }
}