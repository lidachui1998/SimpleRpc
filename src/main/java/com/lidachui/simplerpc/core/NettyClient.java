package com.lidachui.simplerpc.core;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.concurrent.CompletableFuture;
/**
 * NettyClient
 *
 * @author: lihuijie
 * @date: 2024/8/20 22:54
 * @version: 1.0
 */
public class NettyClient {

    private final String host;
    private final int port;

    public NettyClient(String url) {
        String[] parts = url.split(":");
        this.host = parts[0];
        this.port = Integer.parseInt(parts[1]);
    }

    public Object sendRequest(String methodName, Object[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 处理半包问题的解码器
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4));
                            pipeline.addLast(new LengthFieldPrepender(4));
                            // 编解码器，使用Java序列化方式
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            // 自定义处理器
                            pipeline.addLast(new RpcClientHandler());
                        }
                    });

            ChannelFuture future = b.connect(host, port).sync();
            RpcRequest request = new RpcRequest(methodName, args);
            CompletableFuture<Object> responseFuture = new CompletableFuture<>();
            RpcClientHandler.setResponseFuture(responseFuture);

            future.channel().writeAndFlush(request).sync();
            // 等待服务端返回的响应结果
            return responseFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            group.shutdownGracefully();
        }
    }
}
