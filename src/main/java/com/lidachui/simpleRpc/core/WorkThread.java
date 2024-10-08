package com.lidachui.simpleRpc.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WorkThread
 *
 * @author: lihuijie
 * @date: 2024/8/26 10:20
 * @version: 1.0
 */
@AllArgsConstructor
public class WorkThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WorkThread.class);


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
            logger.error("从IO中读取数据错误", e);
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
            method =
                    service.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
            Object invoke = method.invoke(service, request.getParams());
            return RpcResponse.success(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            return RpcResponse.fail(e,"RPC:请求报错!");
        }
    }
}
