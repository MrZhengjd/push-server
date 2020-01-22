package com.zheng.msg;

import com.zheng.netty.MessageType;

public class SendMessage extends BaseMessage {

    private String fromUser;
    private String toUser;
    private Object message;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public SendMessage() {

        super(MessageType.SEND);
    }

    @Override
    public String toString() {
        return "SendMessage{" +
                "fromUser='" + fromUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", message=" + message +
                ", createTime=" + createTime +
                ", state=" + state +
                ", messageType=" + messageType +
                ", token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                ", messageId='" + messageId + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
