package com.lidachui.simpleRpc.client;

import com.lidachui.simpleRpc.serialize.JsonSerializer;
import com.lidachui.simpleRpc.serialize.RpcDecode;
import com.lidachui.simpleRpc.serialize.RpcEncode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.AllArgsConstructor;

/**
 * NettyClientInitializer
 *
 * @author: lihuijie
 * @date: 2024/8/26 11:22
 * @version: 1.0
 */
@AllArgsConstructor
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 消息格式 [长度][消息体], 解决粘包问题
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        // 计算当前待发送消息的长度，写入到前4个字节中
        pipeline.addLast(new LengthFieldPrepender(4));

        //    // 这里使用的还是java 序列化方式， netty的自带的解码编码支持传输这种结构
        //    pipeline.addLast(new ObjectEncoder());
        //    pipeline.addLast(new ObjectDecoder(className -> Class.forName(className)));
        // 使用自定义的编解码器
        pipeline.addLast(new RpcDecode());
        // 编码需要传入序列化器，这里是json，还支持ObjectSerializer，也可以自己实现其他的
        pipeline.addLast(new RpcEncode(new JsonSerializer()));
        pipeline.addLast(new NettyClientHandler());
    }
}
