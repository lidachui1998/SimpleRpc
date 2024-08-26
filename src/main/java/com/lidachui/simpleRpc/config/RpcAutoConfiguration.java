package com.lidachui.simpleRpc.config;


import com.lidachui.simpleRpc.annotation.RpcReference;
import com.lidachui.simpleRpc.annotation.RpcService;
import com.lidachui.simpleRpc.client.NettyRpcClient;
import com.lidachui.simpleRpc.condition.OnRpcClientCondition;
import com.lidachui.simpleRpc.condition.OnRpcServerCondition;
import com.lidachui.simpleRpc.core.RpcClient;
import com.lidachui.simpleRpc.core.RpcClientProxy;
import com.lidachui.simpleRpc.core.RpcServer;
import com.lidachui.simpleRpc.core.ServiceProvider;
import com.lidachui.simpleRpc.server.NettyRpcServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;

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

    @Autowired
    private RpcServerProperties rpcServerProperties;

    @Autowired
    private Environment environment;
    @Bean
    @Conditional(OnRpcClientCondition.class)
    public RpcClient rpcClient() {
        String mainClassName = System.getProperty("sun.java.command").split(" ")[0];
        try {
            Class<?> mainClass = Class.forName(mainClassName);
            com.lidachui.simpleRpc.annotation.RpcClient annotation = mainClass.getAnnotation(com.lidachui.simpleRpc.annotation.RpcClient.class);
            if (annotation != null) {
                String host = annotation.host();
                int port = annotation.port();
                NettyRpcClient nettyRpcClient = new NettyRpcClient(host, port);
                return nettyRpcClient;
            }
        } catch (ClassNotFoundException e) {
            // 处理异常
            return null;
        }
        return null;
    }

    @Bean
    @ConditionalOnMissingBean(ServiceProvider.class)
    @Conditional(OnRpcServerCondition.class)
    public RpcServer rpcServer(ServiceProvider serviceProvider) {
        String mainClassName = System.getProperty("sun.java.command").split(" ")[0];
        try {
            Class<?> mainClass = Class.forName(mainClassName);
            com.lidachui.simpleRpc.annotation.RpcServer annotation = mainClass.getAnnotation(com.lidachui.simpleRpc.annotation.RpcServer.class);
            if (annotation != null) {
                int port = annotation.port();
                RpcServer RPCServer = new NettyRpcServer(serviceProvider);
                RPCServer.start(port);
                return RPCServer;
            }
        } catch (ClassNotFoundException e) {
            // 处理异常
            return null;
        }
        return null;
    }

    @Bean
    @ConditionalOnMissingBean(RpcClient.class)
    public RpcClientProxy rpcClientProxy(RpcClient rpcClient) {
        return new RpcClientProxy(rpcClient);
    }

    @Bean
    @ConditionalOnMissingBean(RpcClientProxy.class)
    public BeanPostProcessor rpcReferenceProcessor(RpcClientProxy rpcClientProxy) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                Field[] fields = bean.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(RpcReference.class)) {
                        field.setAccessible(true);
                        Object proxy = rpcClientProxy.getProxy(field.getType());
                        try {
                            field.set(bean, proxy);
                        } catch (IllegalAccessException e) {
                            throw new BeansException("Failed to inject RPC proxy", e) {};
                        }
                    }
                }
                return bean;
            }
        };
    }

    @Bean
    public ServiceProvider serviceProvider() {
        return new ServiceProvider();
    }

    @Bean
    @ConditionalOnMissingBean(ServiceProvider.class)
    public BeanPostProcessor rpcServiceProcessor(ServiceProvider serviceProvider) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                Class<?> beanClass = bean.getClass();
                Class<?>[] interfaces = beanClass.getInterfaces();

                for (Class<?> iface : interfaces) {
                    if (iface.isAnnotationPresent(RpcService.class)) {
                        serviceProvider.provideServiceInterface(bean);
                    }
                }
                return bean;
            }
        };
    }
}
