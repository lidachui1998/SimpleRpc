package com.lidachui.simpleRpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RpcClient
 *
 * @author: lihuijie
 * @date: 2024/8/26 21:48
 * @version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcClient {

    String host() default "127.0.0.1";

    int port() default 8899;
}
