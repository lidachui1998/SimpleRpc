package com.lidachui.simpleRpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Resource;

/**
 * RpcAutowired
 *
 * @author: lihuijie
 * @date: 2024/8/26 21:46
 * @version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Resource
public @interface RpcReference {}
