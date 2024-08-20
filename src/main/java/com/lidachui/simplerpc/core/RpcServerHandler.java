package com.lidachui.simplerpc.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Method;
import java.util.*;

/**
 * RpcServerHandler
 *
 * @author: lihuijie
 * @date: 2024/8/20 22:57
 * @version: 1.0
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final Map<String, Object> serviceMap;

    public RpcServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        RpcResponse response = new RpcResponse();
        try {
            Object service = serviceMap.get(request.getClassName());
            if (service == null) {
                throw new ClassNotFoundException("Service not found: " + request.getClassName());
            }

            Method method = service.getClass().getMethod(request.getMethodName(), getParameterTypes(request.getParameters()));
            Object result = method.invoke(service, request.getParameters());
            response.setResult(result);
        } catch (Exception e) {
            response.setException(e);
        }
        ctx.writeAndFlush(response);
    }

    private Class<?>[] getParameterTypes(Object[] parameters) {
        if (parameters == null) {
            return new Class<?>[0];
        }
        Class<?>[] parameterTypes = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = parameters[i].getClass();
        }
        return parameterTypes;
    }
}
