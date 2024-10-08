package com.lidachui.simpleRpc.server;

import com.lidachui.simpleRpc.core.RpcRequest;
import com.lidachui.simpleRpc.core.RpcResponse;
import com.lidachui.simpleRpc.core.ServiceProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NettyRPCServerHandler
 *
 * @author: lihuijie
 * @date: 2024/8/26 11:21
 * @version: 1.0
 */
@AllArgsConstructor
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServerHandler.class);


    private ServiceProvider serviceProvider;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        logger.info("收到客户端请求:{}", msg);
        RpcResponse response = getResponse(msg);
        ctx.writeAndFlush(response);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    RpcResponse getResponse(RpcRequest request) {
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
            logger.error("RPC:请求报错!",e);
            return RpcResponse.fail(e,"RPC:请求报错!");
        }
    }
}
