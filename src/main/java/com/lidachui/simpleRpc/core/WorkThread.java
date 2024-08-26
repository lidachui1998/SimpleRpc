package com.lidachui.simpleRpc.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import lombok.AllArgsConstructor;

/**
 * WorkThread
 *
 * @author: lihuijie
 * @date: 2024/8/26 10:20
 * @version: 1.0
 */

/**
 * 这里负责解析得到的request请求，执行服务方法，返回给客户端 1. 从request得到interfaceName 2. 根据interfaceName在serviceProvide
 * Map中获取服务端的实现类 3. 从request中得到方法名，参数， 利用反射执行服务中的方法 4. 得到结果，封装成response，写入socket
 */
@AllArgsConstructor
public class WorkThread implements Runnable {
  private Socket socket;
  private ServiceProvider serviceProvider;

  @Override
  public void run() {
    try {
      ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
      // 读取客户端传过来的request
      RpcRequest request = (RpcRequest) ois.readObject();
      // 反射调用服务方法获得返回值
      RpcResponse response = getResponse(request);
      // 写入到客户端
      oos.writeObject(response);
      oos.flush();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      System.out.println("从IO中读取数据错误");
    }
  }

  private RpcResponse getResponse(RpcRequest request) {
    // 得到服务名
    String interfaceName = request.getInterfaceName();
    // 得到服务端相应服务实现类
    Object service = serviceProvider.getService(interfaceName);
    // 反射调用方法
    Method method;
    try {
      method = service.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
      Object invoke = method.invoke(service, request.getParams());
      return RpcResponse.success(invoke);
    } catch (Exception e) {
      e.printStackTrace();
      return RpcResponse.fail(e);
    }
  }
}
