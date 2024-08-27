package com.lidachui.simpleRpc.core;

/**
 * RpcServer
 *
 * @author: lihuijie
 * @date: 2024/8/26 16:49
 * @version: 1.0
 */
public interface RpcServer {

    /**
     * 开始
     *
     * @param port 港口
     */
    void start(int port);

    /** 停止 */
    void stop();
}
