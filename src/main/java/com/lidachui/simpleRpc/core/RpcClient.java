package com.lidachui.simpleRpc.core;

import com.lidachui.simpleRpc.client.NettyRpcClient.Callback;

/**
 * RpcClient
 *
 * @author: lihuijie
 * @date: 2024/8/26 16:49
 * @version: 1.0
 */
public interface RpcClient {

    /**
     * 发送请求（阻塞）
     *
     * @param request 请求
     * @return {@code RpcResponse }
     */
    RpcResponse sendRequest(RpcRequest request);

    /**
     * 发送请求(回调)
     *
     * @param request 请求
     * @param callback 回拨
     */
    void sendRequest(RpcRequest request, Callback<RpcResponse> callback);
}
