package com.zheng.redis;

import java.io.Serializable;

public class RedisLogin extends AbstractKey implements Serializable {
    private static final long serialVersionUID = -6709527446790344140L;

    @Override
    public void setObjectKey() {
        this.objectKey="USER";
    }

    @Override
    public void setKey() {
        this.key = getUid();
    }

    public RedisLogin() {
        super();
    }


    private String uid;

    public RedisLogin(String uid) {
        super();
        this.uid = uid;
        setKey();
    }

    private String token;

    private long refTime;

    public RedisLogin(String uid, String token, long refTime) {
        super();
        this.uid = uid;
        this.token = token;
        this.refTime = refTime;
        setKey();
    }

    @Override
    public String toString() {
        return "RedisLogin{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", refTime=" + refTime +
                '}';
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

    public long getRefTime() {
        return refTime;
    }

    public void setRefTime(long refTime) {
        this.refTime = refTime;
    }
}
