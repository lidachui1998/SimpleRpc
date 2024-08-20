package com.lidachui.simplerpc.core;

import com.lidachui.simplerpc.core.annotation.RpcService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

/**
 * RpcServer
 *
 * @author: lihuijie
 * @date: 2024/8/20 22:51
 * @version: 1.0
 */
@Component
public class RpcServer implements ApplicationContextAware, InitializingBean {

    private final int port;
    private ApplicationContext applicationContext;
    private final Map<String, Object> serviceMap = new HashMap<>();

    public RpcServer(int port) {
        this.port = port;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scanAndRegisterServices();
        startServer();
    }

    private void scanAndRegisterServices() {
        // 使用 Spring 的扫描器
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RpcService.class));

        for (String basePackage : getBasePackages()) {
            scanner.findCandidateComponents(basePackage).forEach(beanDefinition -> {
                try {
                    Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                    RpcService rpcService = clazz.getAnnotation(RpcService.class);
                    Object serviceBean = applicationContext.getBean(clazz);
                    serviceMap.put(rpcService.value().getName(), serviceBean);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void startServer() {
        new NettyServer(port, serviceMap).start();
    }

    private String[] getBasePackages() {
        // 这里指定扫描的基础包路径，可以动态配置
        return new String[]{"com.example.serverapp.service"};
    }
}
