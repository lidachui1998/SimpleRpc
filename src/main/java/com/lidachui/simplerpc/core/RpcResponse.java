package com.lidachui.simplerpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * RpcResponse
 *
 * @author: lihuijie
 * @date: 2024/8/20 22:59
 * @version: 1.0
 */
@Data
public class RpcResponse implements Serializable {
    private Object result;
    private Exception exception;

}