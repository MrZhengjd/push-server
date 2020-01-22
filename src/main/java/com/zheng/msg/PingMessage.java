package com.zheng.msg;

import com.zheng.netty.MessageType;

public class PingMessage extends BaseMessage {
    private  String name;

    public PingMessage() {
        super(MessageType.PING);
    }

    public PingMessage(String name) {
        this();
        this.name = name;
    }

    @Override
    public String toString() {
        return "PingMessage{" +
                "name='" + name + '\'' +
                ", createTime=" + createTime +
                ", state=" + state +
                '}';
    }
}
