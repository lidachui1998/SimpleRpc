package com.lidachui.simplerpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * RpcRequest
 *
 * @author: lihuijie
 * @date: 2024/8/20 22:58
 * @version: 1.0
 */
@Data
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private String className;
    private String methodName;
    private Object[] parameters;

    public RpcRequest(String methodName, Object[] parameters) {
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }
}
