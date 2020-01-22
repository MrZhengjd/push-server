package com.zheng.msg;

import com.zheng.netty.MessageType;

/**
 * 存在引用计数的问题 最终导致内存爆掉 频繁gc的问题
 */
public abstract class BaseMessage {
    protected Long createTime;
    protected int state;
    protected MessageType messageType ;
    protected String token;
    protected String uid;
    protected String messageId;
    protected String requestId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }



    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 0 text 1jpg
     */
    private byte mime;

    public byte getMime() {
        return mime;
    }

    public void setMime(byte mime) {
        this.mime = mime;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    protected BaseMessage(MessageType messageType) {
        this.messageType = messageType;
        mime = 0;
        createTime = System.currentTimeMillis();

    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
