package com.zheng.redis;

import io.netty.channel.Channel;

import java.io.Serializable;

public class Session extends AbstractKey implements Serializable {
    private static final long serialVersionUID = 5329239194031869754L;

    private String sessionId;
    private Channel channel = null;
    private long lastCommunicationTime = 0l;

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", channel=" + channel +
                ", lastCommunicationTime=" + lastCommunicationTime +
                '}';
    }

    public static Session buildSession(Channel channel){
        Session session = new Session();
        session.setChannel(channel);
        session.setSessionId(channel.id().asLongText());
        session.setLastCommunicationTime(System.currentTimeMillis());
        return session;
    }
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public long getLastCommunicationTime() {
        return lastCommunicationTime;
    }

    public void setLastCommunicationTime(long lastCommunicationTime) {
        this.lastCommunicationTime = lastCommunicationTime;
    }

    @Override
    public void setObjectKey() {
        this.objectKey = "SESSION";
    }

    @Override
    public void setKey() {
        this.key = getSessionId();
    }
}
