package com.lidachui.simplerpc.core;


import com.lidachui.simplerpc.core.annotation.RpcReference;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;

/**
 * RpcClientMain
 *
 * @author: lihuijie
 * @date: 2024/8/19 23:18
 * @version: 1.0
 */
// 示例：Netty RPC 客户端
// RpcClient.java
import java.lang.reflect.Field;

@Component
public class RpcClient extends InstantiationAwareBeanPostProcessorAdapter {

    @Resource
    private RpcClientProxy rpcClientProxy;

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                Object proxy = RpcClientProxy.createProxy(field.getType(), rpcReference.url());
                field.setAccessible(true);
                try {
                    field.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
