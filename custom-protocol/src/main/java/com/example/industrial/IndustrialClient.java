// IndustrialClient.java
package com.example.industrial;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class IndustrialClient {

    private final String host;
    private final int port;
    private Channel channel;

    public IndustrialClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                                new IndustrialProtocolEncoder(),
                                new IndustrialProtocolDecoder(),
                                // 添加字符串编解码器用于日志打印
                                new StringEncoder(),
                                new ClientHandler(IndustrialClient.this)
                        );
                    }
                });

        ChannelFuture future = bootstrap.connect(host, port).sync();
        this.channel = future.channel();
        System.out.println("✅ 已连接到服务端: " + host + ":" + port);
        System.out.println("📩 输入消息格式: <设备ID> <命令码> <数据>");
        System.out.println("     例如: 01 03 hello");
        System.out.println("     输入 'quit' 退出");

        // 开启控制台输入监听
        listenConsoleInput();
    }

    private void listenConsoleInput() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;

        while ((line = in.readLine()) != null) {
            if ("quit".equalsIgnoreCase(line.trim())) {
                System.out.println("👋 正在断开连接...");
                channel.close().sync();
                break;
            }

            if (line.trim().isEmpty()) continue;

            try {
                // 解析输入：设备ID 命令码 数据
                String[] parts = line.trim().split(" ", 3);
                if (parts.length < 3) {
                    System.out.println("❌ 格式错误！请输入: <设备ID> <命令码> <数据>");
                    continue;
                }

                byte deviceId = (byte) Integer.parseInt(parts[0], 16);
                byte command = (byte) Integer.parseInt(parts[1], 16);
                String dataStr = parts[2];

                // 创建消息并发送
                IndustrialMessage message = new IndustrialMessage();
                message.setStart((byte) 0xAA);
                message.setDeviceId(deviceId);
                message.setCommand(command);

                // 写入数据
                io.netty.buffer.ByteBuf dataBuf = Unpooled.buffer();
                dataBuf.writeBytes(dataStr.getBytes("UTF-8"));
                message.setData(dataBuf);

                // 发送
                channel.writeAndFlush(message);
                System.out.println("📤 已发送 -> 设备:" + String.format("%02X", deviceId) +
                        " 命令:" + String.format("%02X", command) +
                        " 数据:" + dataStr);

            } catch (NumberFormatException e) {
                System.out.println("❌ 设备ID 或 命令码 必须是十六进制字节 (如 01, 03)");
            } catch (Exception e) {
                System.out.println("❌ 发送失败: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 9090;

        if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        new IndustrialClient(host, port).start();
    }
}