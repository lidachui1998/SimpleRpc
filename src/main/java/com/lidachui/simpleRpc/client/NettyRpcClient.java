package com.lidachui.simpleRpc.client;

import static com.lidachui.simpleRpc.constants.Constants.NETTY_KEY;

import com.lidachui.simpleRpc.core.RpcClient;
import com.lidachui.simpleRpc.core.RpcRequest;
import com.lidachui.simpleRpc.core.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/**
 * NettyRPCClient
 *
 * @author: lihuijie
 * @date: 2024/8/26 11:21
 * @version: 1.0
 */

/** 实现RPCClient接口 */
public class NettyRpcClient implements RpcClient {
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    private String host;
    private int port;

    public NettyRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // Netty 客户端初始化，重复使用
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            // 发送数据
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题。
            // 实际上不应通过阻塞，可通过回调函数
            AttributeKey<RpcResponse> key = AttributeKey.valueOf(NETTY_KEY);
            RpcResponse response = channel.attr(key).get();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return RpcResponse.fail(e,"RPC请求发送失败");
        }
    }

    @Override
    public void sendRequest(RpcRequest request, Callback<RpcResponse> callback) {
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();

            // 创建一个 ChannelPromise 用于异步接收响应
            final ChannelPromise promise = channel.newPromise();

            // 在 promise 完成时执行回调
            promise.addListener(
                    future -> {
                        if (future.isSuccess()) {
                            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
                            RpcResponse response = channel.attr(key).get();
                            callback.onSuccess(response);
                        } else {
                            callback.onFailure(future.cause());
                        }
                    });

            // 发送请求并在 handler 中设置响应
            channel.writeAndFlush(request)
                    .addListener(
                            writeFuture -> {
                                if (!writeFuture.isSuccess()) {
                                    promise.setFailure(writeFuture.cause());
                                }
                            });

            // 关闭通道时，标记 promise 完成
            channel.closeFuture().addListener(closeFuture -> promise.setSuccess());
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    public interface Callback<T> {
        void onSuccess(T result);

        void onFailure(Throwable t);
    }
}
