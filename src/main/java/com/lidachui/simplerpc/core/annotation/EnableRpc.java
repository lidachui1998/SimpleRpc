package com.lidachui.simplerpc.core.annotation;

import com.lidachui.simplerpc.core.RpcAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EnableRpc
 *
 * @author: lihuijie
 * @date: 2024/8/20 23:43
 * @version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcAutoConfiguration.class)
public @interface EnableRpc {
}