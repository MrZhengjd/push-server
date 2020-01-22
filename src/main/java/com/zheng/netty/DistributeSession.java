package com.zheng.netty;

import com.zheng.redis.AbstractKey;
import com.zheng.server.AbstractServer;
import io.netty.channel.Channel;

import java.io.Serializable;

public class DistributeSession extends AbstractKey implements Serializable {
    private static final long serialVersionUID = -8095686553968078507L;

    private String uid;
    private String sessionId;
    private long serverId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    @Override
    public void setObjectKey() {
        this.objectKey = "DISTRIBUTE";
    }

    @Override
    public void setKey() {
        this.key = getUid();
    }
    public static DistributeSession build(Channel channel,String uid){
        DistributeSession session = new DistributeSession();
        session.setServerId(AbstractServer.serverId);
        session.setSessionId(channel.id().asLongText());
        session.setUid(uid);
        session.setKey();
        return session;
    }
    public static DistributeSession buildNull(){
        DistributeSession session = new DistributeSession();
        return session;
    }
}
