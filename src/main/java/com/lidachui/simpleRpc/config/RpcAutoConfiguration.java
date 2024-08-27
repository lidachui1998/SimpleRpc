package com.lidachui.simpleRpc.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lidachui.simpleRpc.client.NettyRpcClient;
import com.lidachui.simpleRpc.condition.OnRpcClientCondition;
import com.lidachui.simpleRpc.condition.OnRpcServerCondition;
import com.lidachui.simpleRpc.core.RpcClient;
import com.lidachui.simpleRpc.core.RpcClientProxy;
import com.lidachui.simpleRpc.core.RpcReferenceProcessor;
import com.lidachui.simpleRpc.core.RpcServer;
import com.lidachui.simpleRpc.core.RpcServiceProcessor;
import com.lidachui.simpleRpc.core.ServiceProvider;
import com.lidachui.simpleRpc.server.NettyRpcServer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * RpcAutoConfiguration
 *
 * @author: lihuijie
 * @date: 2024/8/26 21:49
 * @version: 1.0
 */
@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcAutoConfiguration {

    @Bean
    @Conditional(OnRpcClientCondition.class)
    public RpcClient rpcClient() {
        String mainClassName = System.getProperty("sun.java.command").split(" ")[0];
        try {
            Class<?> mainClass = Class.forName(mainClassName);
            com.lidachui.simpleRpc.annotation.RpcClient annotation =
                    mainClass.getAnnotation(com.lidachui.simpleRpc.annotation.RpcClient.class);
            if (annotation != null) {
                String host = annotation.host();
                int port = annotation.port();
                return new NettyRpcClient(host, port);
            }
        } catch (ClassNotFoundException e) {
            // 处理异常
            return null;
        }
        return null;
    }

    @Bean(destroyMethod = "shutdown", name = "simpleRpcThreadPool")
    @Conditional(OnRpcServerCondition.class)
    public ThreadPoolExecutor simpleRpcThreadPool() {
        ThreadFactory tf =
                new ThreadFactoryBuilder().setNameFormat("simpleRpcThreadPool-%d").build();
        return new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                200,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(10),
                tf,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean
    @Conditional(OnRpcServerCondition.class)
    public RpcServer rpcServer(
            ServiceProvider serviceProvider, ThreadPoolExecutor simpleRpcThreadPool) {
        String mainClassName = System.getProperty("sun.java.command").split(" ")[0];
        try {
            Class<?> mainClass = Class.forName(mainClassName);
            com.lidachui.simpleRpc.annotation.RpcServer annotation =
                    mainClass.getAnnotation(com.lidachui.simpleRpc.annotation.RpcServer.class);
            if (annotation != null) {
                int port = annotation.port();
                RpcServer RPCServer = new NettyRpcServer(serviceProvider);
                simpleRpcThreadPool.execute(() -> RPCServer.start(port));
                return RPCServer;
            }
        } catch (ClassNotFoundException e) {
            // 处理异常
            return null;
        }
        return null;
    }

    @Bean
    @Conditional(OnRpcClientCondition.class)
    public RpcClientProxy rpcClientProxy(RpcClient rpcClient) {
        return new RpcClientProxy(rpcClient);
    }

    @Bean
    @Conditional(OnRpcServerCondition.class)
    public ServiceProvider serviceProvider() {
        return new ServiceProvider();
    }

    @Bean
    @Conditional(OnRpcClientCondition.class)
    public RpcReferenceProcessor rpcReferenceProcessor(RpcClientProxy rpcClientProxy) {
        return new RpcReferenceProcessor(rpcClientProxy);
    }

    @Bean
    @Conditional(OnRpcServerCondition.class)
    public RpcServiceProcessor rpcServiceProcessor(ServiceProvider serviceProvider) {
        return new RpcServiceProcessor(serviceProvider);
    }
}
