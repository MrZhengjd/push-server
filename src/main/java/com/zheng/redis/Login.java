package com.zheng.redis;

import java.io.Serializable;

public class Login implements Serializable {
    private static final long serialVersionUID = -5846892564509614994L;
    private String uid;
    private String userName;

    public Login() {
        super();
    }


    @Override
    public String toString() {
        return "Login{" +
                "uid='" + uid + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Login(String uid, String userName) {
        super();
        this.uid = uid;
        this.userName = userName;
    }
}
