package com.zheng.msg;

import com.zheng.netty.MessageType;

public class AuthMessage extends BaseMessage {
    @Override
    public String toString() {
        return "AuthMessage{" +
                "responseVo=" + responseVo +
                ", createTime=" + createTime +
                ", state=" + state +
                ", messageType=" + messageType +
                ", token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                ", messageId='" + messageId + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }

    public AuthMessage() {
        super(MessageType.AUTH);
    }
    private ResponseVo responseVo;

    public ResponseVo getResponseVo() {
        return responseVo;
    }

    public void setResponseVo(ResponseVo responseVo) {
        this.responseVo = responseVo;
    }
}
