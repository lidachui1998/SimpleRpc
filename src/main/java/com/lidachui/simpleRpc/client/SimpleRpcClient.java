package com.lidachui.simpleRpc.client;

import com.lidachui.simpleRpc.client.NettyRpcClient.Callback;
import com.lidachui.simpleRpc.core.RpcClient;
import com.lidachui.simpleRpc.core.RpcRequest;
import com.lidachui.simpleRpc.core.RpcResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import lombok.AllArgsConstructor;

/**
 * SimpleRpcClient
 *
 * @author: lihuijie
 * @date: 2024/8/26 10:51
 * @version: 1.0
 */
@AllArgsConstructor
public class SimpleRpcClient implements RpcClient {
  private String host;
  private int port;

  // 客户端发起一次请求调用，Socket建立连接，发起请求Request，得到响应Response
  // 这里的request是封装好的，不同的service需要进行不同的封装， 客户端只知道Service接口，需要一层动态代理根据反射封装不同的Service
  public RpcResponse sendRequest(RpcRequest request) {
    try {
      // 发起一次Socket连接请求
      Socket socket = new Socket(host, port);

      ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

      System.out.println(request);
      objectOutputStream.writeObject(request);
      objectOutputStream.flush();

      RpcResponse response = (RpcResponse) objectInputStream.readObject();

      // System.out.println(response.getData());
      return response;
    } catch (IOException | ClassNotFoundException e) {
      System.out.println();
      return null;
    }
  }

  /**
   * 发送请求(回调)
   *
   * @param request  请求
   * @param callback 回拨
   */
  @Override
  public void sendRequest(RpcRequest request, Callback<RpcResponse> callback) {
    try {
      // 发起一次Socket连接请求
      Socket socket = new Socket(host, port);

      ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

      System.out.println(request);
      objectOutputStream.writeObject(request);
      objectOutputStream.flush();

      RpcResponse response = (RpcResponse) objectInputStream.readObject();

      // System.out.println(response.getData());
      callback.onSuccess(response);
    } catch (IOException | ClassNotFoundException e) {
      System.out.println();
      callback.onFailure(e);
    }
  }
}
