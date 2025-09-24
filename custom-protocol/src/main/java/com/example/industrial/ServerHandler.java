package com.example.industrial;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<IndustrialMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IndustrialMessage msg) throws Exception {
        System.out.println("服务器收到: " + msg);

        // 回复一个响应（命令码 + 0x80）
        byte[] responseData = ("Echo: " + msg.getData().toString(io.netty.util.CharsetUtil.UTF_8)).getBytes();
        ByteBuf data = ctx.alloc().buffer().writeBytes(responseData);
        IndustrialMessage response = new IndustrialMessage(msg.getDeviceId(), (byte)(msg.getCommand() + (byte)0x80), data);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}