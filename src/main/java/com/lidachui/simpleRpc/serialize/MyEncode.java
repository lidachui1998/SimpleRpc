package com.lidachui.simpleRpc.serialize;

import com.lidachui.simpleRpc.constants.MessageType;
import com.lidachui.simpleRpc.core.RpcRequest;
import com.lidachui.simpleRpc.core.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

/**
 * MyEncode
 *
 * @author: lihuijie
 * @date: 2024/8/26 13:15
 * @version: 1.0
 */
/** 依次按照自定义的消息格式写入，传入的数据为request或者response 需要持有一个serialize器，负责将传入的对象序列化成字节数组 */
@AllArgsConstructor
public class MyEncode extends MessageToByteEncoder {
  private Serializer serializer;

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
    System.out.println(msg.getClass());
    // 写入消息类型
    if (msg instanceof RpcRequest) {
      out.writeShort(MessageType.REQUEST.getCode());
    } else if (msg instanceof RpcResponse) {
      out.writeShort(MessageType.RESPONSE.getCode());
    }
    // 写入序列化方式
    out.writeShort(serializer.getType());
    // 得到序列化数组
    byte[] serialize = serializer.serialize(msg);
    // 写入长度
    out.writeInt(serialize.length);
    // 写入序列化字节数组
    out.writeBytes(serialize);
  }
}
