package com.lidachui.simpleRpc.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * RPCClientProxy
 *
 * @author: lihuijie
 * @date: 2024/8/26 10:57
 * @version: 1.0
 */
public class RpcClientProxy implements InvocationHandler {

    private RpcClient client;

    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 处理 Object 类的方法，如 toString, equals, hashCode
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        if (client != null) {
            // request的构建，使用了lombok中的builder，代码简洁
            RpcRequest request =
                    RpcRequest.builder()
                            .interfaceName(method.getDeclaringClass().getName())
                            .methodName(method.getName())
                            .params(args)
                            .paramsTypes(method.getParameterTypes())
                            .build();
            RpcResponse response = client.sendRequest(request);
            return response.getData();
        }
        return RpcResponse.fail();
    }

    /**
     * 获取代理
     *
     * @param clazz clazz
     * @return {@code T }
     */
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, this);
    }
}
