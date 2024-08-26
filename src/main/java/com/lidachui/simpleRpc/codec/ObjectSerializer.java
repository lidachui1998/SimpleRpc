package com.lidachui.simpleRpc.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * ObjectSerializer
 *
 * @author: lihuijie
 * @date: 2024/8/26 16:42
 * @version: 1.0
 */
public class ObjectSerializer implements Serializer{

  // 利用java IO 对象 -> 字节数组
  @Override
  public byte[] serialize(Object obj) {
    byte[] bytes = null;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(obj);
      oos.flush();
      bytes = bos.toByteArray();
      oos.close();
      bos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return bytes;
  }

  // 字节数组 -> 对象
  @Override
  public Object deserialize(byte[] bytes, int messageType) {
    Object obj = null;
    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    try {
      ObjectInputStream ois = new ObjectInputStream(bis);
      obj = ois.readObject();
      ois.close();
      bis.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return obj;
  }

  // 0 代表java原生序列化器
  @Override
  public int getType() {
    return 0;
  }
}
