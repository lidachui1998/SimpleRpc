package com.lidachui.simpleRpc.server;

import com.lidachui.simpleRpc.core.RpcServer;
import com.lidachui.simpleRpc.core.ServiceProvider;
import com.lidachui.simpleRpc.core.WorkThread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolRPCRPCServer
 *
 * @author: lihuijie
 * @date: 2024/8/26 10:23
 * @version: 1.0
 */
public class ThreadPoolRpcRpcServer implements RpcServer {
    private final ThreadPoolExecutor threadPool;
    private ServiceProvider serviceProvider;

    public ThreadPoolRpcRpcServer(ServiceProvider serviceProvider) {
        threadPool =
                new ThreadPoolExecutor(
                        Runtime.getRuntime().availableProcessors(),
                        1000,
                        60,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(100));
        this.serviceProvider = serviceProvider;
    }

    public ThreadPoolRpcRpcServer(
            ServiceProvider serviceProvider,
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue) {

        threadPool =
                new ThreadPoolExecutor(
                        corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        System.out.println("服务端启动了");
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new WorkThread(socket, serviceProvider));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {}
}
