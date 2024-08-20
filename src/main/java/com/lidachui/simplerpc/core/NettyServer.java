package com.lidachui.simplerpc.core;

/**
 * NettyServer
 *
 * @author: lihuijie
 * @date: 2024/8/20 22:53
 * @version: 1.0
 */
// NettyServer.java
import com.lidachui.simplerpc.core.RpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.SocketChannel;

import java.util.HashMap;
import java.util.Map;

public class NettyServer {

    private final int port;
    private static final Map<String, Object> serviceMap = new HashMap<>();

    public NettyServer(int port, Map<String, Object> serviceMap) {
        this.port = port;
        this.serviceMap.putAll(serviceMap);
    }

    public static void registerService(String serviceName, Object service) {
        serviceMap.put(serviceName, service);
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new RpcServerHandler(serviceMap));
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
