package com.lidachui.simpleRpc.condition;

import com.lidachui.simpleRpc.annotation.RpcClient;
import com.lidachui.simpleRpc.annotation.RpcServer;
import com.lidachui.simpleRpc.annotation.RpcService;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;

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
