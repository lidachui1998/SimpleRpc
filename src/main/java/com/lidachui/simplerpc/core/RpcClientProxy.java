package com.lidachui.simplerpc.core;

import com.lidachui.simplerpc.core.NettyClient;

import java.lang.reflect.Proxy;

/**
 * RpcClientProxy
 *
 * @author: lihuijie
 * @date: 2024/8/20 22:52
 * @version: 1.0
 */
public class RpcClientProxy {

    public static <T> T createProxy(Class<T> interfaceClass, String url) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
                    NettyClient client = new NettyClient(url);
                    return client.sendRequest(method.getName(), args);
                }
        );
    }
}