package com.zheng.msg;

import com.zheng.netty.MessageType;


public class AckMessage extends BaseMessage {
    @Override
    public String toString() {
        return "AckMessage{" +
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

    public AckMessage() {

        super(MessageType.ACK);
    }
    private ResponseVo responseVo;

    public AckMessage( ResponseVo responseVo) {
        super(MessageType.ACK);
        this.responseVo = responseVo;
    }

    public ResponseVo getResponseVo() {
        return responseVo;
    }

    public void setResponseVo(ResponseVo responseVo) {
        this.responseVo = responseVo;
    }
    public static AckMessage buildSuccess(BaseMessage baseMessage){
        AckMessage ackMessage = (AckMessage) baseMessage;
        ackMessage.setMessageType(MessageType.ACK);
        ackMessage.setUid(baseMessage.getUid());
        ackMessage.setMessageId(baseMessage.getMessageId());
        ackMessage.setRequestId(baseMessage.getRequestId());
        return ackMessage;
    }
    public static AckMessage buildFail(BaseMessage baseMessage){
        AckMessage ackMessage = (AckMessage) baseMessage;
        ackMessage.setMessageType(MessageType.ACK);
        ackMessage.setResponseVo(ResponseVo.fail("error"));
        return ackMessage;
    }
}
