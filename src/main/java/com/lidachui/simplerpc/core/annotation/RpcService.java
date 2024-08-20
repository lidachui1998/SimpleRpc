package com.lidachui.simplerpc.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RpcClient
 *
 * @author: lihuijie
 * @date: 2024/8/20 22:21
 * @version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcService {
  Class<?> value(); // 指定服务接口类型
}
