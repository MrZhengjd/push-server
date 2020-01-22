package com.zheng.netty;

public enum MessageType {
    /**
     * AUTH for design no message send this type
     * because they send it on webgate
     * here for authhandler
     */
    PING((byte)1),PONG((byte)2),SEND((byte)3),ACK((byte)4),PUSH((byte)4),AUTH((byte)5);
    public byte value;

    MessageType(byte i) {
        this.value = i;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
    public static MessageType fromName(byte typeName) {
        for (MessageType type : MessageType.values()) {
            if (type.getValue() == typeName) {
                return type;
            }
        }
        return null;
    }
}
