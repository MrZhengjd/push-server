package com.zheng.msg;

import com.zheng.netty.MessageType;

public class PongMessage extends BaseMessage {
    public PongMessage() {
        super(MessageType.PONG);
    }
}
