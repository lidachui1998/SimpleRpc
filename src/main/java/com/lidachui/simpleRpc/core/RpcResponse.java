package com.lidachui.simpleRpc.core;

import com.lidachui.simpleRpc.common.LogExceptionUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RpcResponse
 *
 * @author: lihuijie
 * @date: 2024/8/26 16:34
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse {

    /** 代码 */
    private Integer code;

    /** 消息 */
    private String message;

    /** 数据类型 */
    private Class<?> dataType;

    /** 数据 */
    private Object data;

    /**
     * 成功
     *
     * @param data 数据
     * @return {@code RpcResponse }
     */
    public static RpcResponse success(Object data) {
        return RpcResponse.builder().code(200).data(data).dataType(data.getClass()).build();
    }

    /**
     * 失败
     *
     * @param ex 前任
     * @param otherMsgValues 其他msg值
     * @return {@code RpcResponse }
     */
    public static RpcResponse fail(Throwable ex, String... otherMsgValues) {
        StringBuilder printMessage = new StringBuilder();
        for (String otherMsgValue : otherMsgValues) {
            printMessage.append("=====").append(otherMsgValue).append("===== \n");
        }
        try {
            printMessage.append(LogExceptionUtil.getExceptionMessage(ex));
        } catch (Exception e) {
            printMessage
                    .append("Error occurred while getting exception message: ")
                    .append(e.getMessage());
        }
        return RpcResponse.builder().code(500).message(printMessage.toString()).dataType(String.class).build();
    }

    /**
     * 失败
     *
     * @param otherMsgValues 其他msg值
     * @return {@code RpcResponse }
     */
    public static RpcResponse fail(String... otherMsgValues) {
        return fail(null, otherMsgValues);
    }

    /**
     * 失败
     *
     * @return {@code RpcResponse }
     */
    public static RpcResponse fail() {
        return RpcResponse.builder().code(500).message("服务器发生错误").dataType(String.class).build();
    }
}
