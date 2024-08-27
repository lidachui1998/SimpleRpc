package com.lidachui.simpleRpc.condition;

import com.lidachui.simpleRpc.annotation.RpcServer;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * OnRpcServerCondition
 *
 * @author: lihuijie
 * @date: 2024/8/26 21:55
 * @version: 1.0
 */
public class OnRpcServerCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String mainClassName = System.getProperty("sun.java.command").split(" ")[0];
        try {
            Class<?> mainClass = Class.forName(mainClassName);
            if (mainClass.isAnnotationPresent(RpcServer.class)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
