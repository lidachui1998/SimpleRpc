package com.lidachui.simplerpc.core;

/**
 * RpcAutoConfiguration
 *
 * @author: lihuijie
 * @date: 2024/8/20 23:40
 * @version: 1.0
 */
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RpcClient rpcClient() {
        // 这里可以从配置文件中读取 URL 配置
        return new RpcClient();
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcServer rpcServer() {
        // 默认端口，可以从配置文件中读取
        return new RpcServer(8080);
    }
}
