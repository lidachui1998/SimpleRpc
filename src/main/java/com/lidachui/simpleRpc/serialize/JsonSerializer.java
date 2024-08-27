package com.lidachui.simpleRpc.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lidachui.simpleRpc.core.RpcRequest;
import com.lidachui.simpleRpc.core.RpcResponse;

/**
 * JsonSerializer
 *
 * @author: lihuijie
 * @date: 2024/8/26 16:42
 * @version: 1.0
 */
public class JsonSerializer implements Serializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化失败", e);
        }
    }

    /**
     * 反序列化
     *
     * @param bytes 字节
     * @param messageType 消息类型
     * @return {@code Object }
     */
    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        try {
            Object obj;
            // 传输的消息分为request与response
            switch (messageType) {
                case 0:
                    RpcRequest request = objectMapper.readValue(bytes, RpcRequest.class);
                    Object[] objects = new Object[request.getParams().length];
                    for (int i = 0; i < objects.length; i++) {
                        Class<?> paramsType = request.getParamsTypes()[i];
                        // Jackson处理转换为Java对象
                        if (!paramsType.isAssignableFrom(request.getParams()[i].getClass())) {
                            objects[i] =
                                    objectMapper.convertValue(request.getParams()[i], paramsType);
                        } else {
                            objects[i] = request.getParams()[i];
                        }
                    }
                    request.setParams(objects);
                    obj = request;
                    break;
                case 1:
                    RpcResponse response = objectMapper.readValue(bytes, RpcResponse.class);
                    Class<?> dataType = response.getDataType();
                    // Jackson处理转换为Java对象
                    if (!dataType.isAssignableFrom(response.getData().getClass())) {
                        response.setData(objectMapper.convertValue(response.getData(), dataType));
                    }
                    obj = response;
                    break;
                default:
                    System.out.println("暂时不支持此种消息");
                    throw new RuntimeException("暂时不支持此种消息");
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("反序列化失败", e);
        }
    }

    // 1 代表着json序列化方式
    @Override
    public int getType() {
        return 1;
    }
}
