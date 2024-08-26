package com.lidachui.simpleRpc.server;


import com.lidachui.simpleRpc.serialize.JsonSerializer;
import com.lidachui.simpleRpc.serialize.MyDecode;
import com.lidachui.simpleRpc.serialize.MyEncode;
import com.lidachui.simpleRpc.core.ServiceProvider;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.AllArgsConstructor;


/**
 * NettyServerInitializer
 *
 * @author: lihuijie
 * @date: 2024/8/26 11:20
 * @version: 1.0
 */

/**
 * 初始化，主要负责序列化的编码解码， 需要解决netty的粘包问题
 */
@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
  private ServiceProvider serviceProvider;
  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    // 消息格式 [长度][消息体], 解决粘包问题
    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
    // 计算当前待发送消息的长度，写入到前4个字节中
    pipeline.addLast(new LengthFieldPrepender(4));

//    // 这里使用的还是java 序列化方式， netty的自带的解码编码支持传输这种结构
//    pipeline.addLast(new ObjectEncoder());
//    pipeline.addLast(new ObjectDecoder(className -> Class.forName(className)));
    // 使用自定义的编解码器
    pipeline.addLast(new MyDecode());
    // 编码需要传入序列化器，这里是json，还支持ObjectSerializer，也可以自己实现其他的
    pipeline.addLast(new MyEncode(new JsonSerializer()));
    pipeline.addLast(new NettyRpcServerHandler(serviceProvider));
  }
}
