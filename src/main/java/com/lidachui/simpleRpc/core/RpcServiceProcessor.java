package com.lidachui.simpleRpc.core;

import com.lidachui.simpleRpc.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

public class RpcServiceProcessor implements BeanPostProcessor {

    private final ServiceProvider serviceProvider;

    // 注入 ServiceProvider
    public RpcServiceProcessor(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        Class<?> beanClass = bean.getClass();
        Class<?>[] interfaces = beanClass.getInterfaces();

        for (Class<?> iface : interfaces) {
            if (iface.isAnnotationPresent(RpcService.class)) {
                try {
                    serviceProvider.provideServiceInterface(bean);
                    break;
                } catch (Exception e) {
                    // 记录错误日志并继续处理其他Bean
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
