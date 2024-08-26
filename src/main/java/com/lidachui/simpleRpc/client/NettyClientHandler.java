package com.lidachui.simpleRpc.client;

import static com.lidachui.simpleRpc.constants.Constants.NETTY_KEY;

import com.lidachui.simpleRpc.core.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 * NettyClientHandler
 *
 * @author: lihuijie
 * @date: 2024/8/26 11:23
 * @version: 1.0
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
    // 接收到response, 给channel设计别名，让sendRequest里读取response
    AttributeKey<RpcResponse> key = AttributeKey.valueOf(NETTY_KEY);
    ctx.channel().attr(key).set(msg);
    ctx.channel().close();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
