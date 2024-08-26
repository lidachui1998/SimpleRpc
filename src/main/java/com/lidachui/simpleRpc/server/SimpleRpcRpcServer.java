package com.lidachui.simpleRpc.server;

import com.lidachui.simpleRpc.core.RpcServer;
import com.lidachui.simpleRpc.core.ServiceProvider;
import com.lidachui.simpleRpc.core.WorkThread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * SimpleRPCRPCServer
 *
 * @author: lihuijie
 * @date: 2024/8/26 10:18
 * @version: 1.0 这个实现类代表着java原始的BIO监听模式，来一个任务，就new一个线程去处理 处理任务的工作见WorkThread中
 */
public class SimpleRpcRpcServer implements RpcServer {
  // 存着服务接口名-> service对象的map
  private ServiceProvider serviceProvider;

  public SimpleRpcRpcServer(ServiceProvider serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  public void start(int port) {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      System.out.println("服务端启动了");
      // BIO的方式监听Socket
      while (true) {
        Socket socket = serverSocket.accept();
        // 开启一个新线程去处理
        new Thread(new WorkThread(socket, serviceProvider)).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("服务器启动失败");
    }
  }

  public void stop() {}
}
