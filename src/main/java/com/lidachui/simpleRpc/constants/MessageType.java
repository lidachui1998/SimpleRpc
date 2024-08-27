package com.lidachui.simpleRpc.constants;

/**
 * MessageType
 *
 * @author: lihuijie
 * @date: 2024/8/26 13:19
 * @version: 1.0
 */
public enum MessageType {
    REQUEST(0, "Request"),
    RESPONSE(1, "Response"),
    HEARTBEAT(2, "heartbeat"),
    PING(3, "ping"),
    PONG(4, "pong"),
    UNKNOWN(5, "unknown"),
    ;

    private final Integer code;

    private final String message;

    MessageType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static MessageType getMessageTypeByCode(String code) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.getCode().equals(code)) {
                return messageType;
            }
        }
        return UNKNOWN;
    }
}
