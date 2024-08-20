package com.lidachui.simplerpc.core;

/**
 * RpcClientHandler
 *
 * @author: lihuijie
 * @date: 2024/8/20 23:02
 * @version: 1.0
 */
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CompletableFuture;

public class RpcClientHandler extends SimpleChannelInboundHandler<Object> {

    private static CompletableFuture<Object> responseFuture;

    public static void setResponseFuture(CompletableFuture<Object> future) {
        responseFuture = future;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        responseFuture.complete(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        responseFuture.completeExceptionally(cause);
        ctx.close();
    }
}
