package com.lidachui.simpleRpc.core;

import com.lidachui.simpleRpc.annotation.RpcReference;
import java.lang.reflect.Field;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * RpcClientProcessor
 *
 * @author: lihuijie
 * @date: 2024/8/27 11:35
 * @version: 1.0
 */
public class RpcReferenceProcessor implements BeanPostProcessor {

    private final RpcClientProxy rpcClientProxy;

    // 构造器注入 RpcClientProxy
    public RpcReferenceProcessor(RpcClientProxy rpcClientProxy) {
        this.rpcClientProxy = rpcClientProxy;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RpcReference.class)) {
                field.setAccessible(true);
                Object proxy = rpcClientProxy.getProxy(field.getType());
                try {
                    field.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
