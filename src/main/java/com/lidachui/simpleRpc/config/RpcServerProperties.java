package com.lidachui.simpleRpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RpcServerProperties
 *
 * @author: lihuijie
 * @date: 2024/8/26 21:58
 * @version: 1.0
 */
@ConfigurationProperties(prefix = "rpc.server")
public class RpcServerProperties {

    private int port = 8080;

    // Getters and Setters
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
