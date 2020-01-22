package com.zheng.redis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

public abstract class AbstractKey implements Serializable {

    @JsonIgnoreProperties(ignoreUnknown = true)
    protected String objectKey;
    @JsonIgnoreProperties(ignoreUnknown = true)
    protected String key;

    public String getKey() {
        return key;
    }

    public abstract void setObjectKey() ;
    public abstract void setKey() ;

    private static final long serialVersionUID = 289520948338797990L;

    public AbstractKey() {
        super();
        setObjectKey();
    }

    protected  String getObjectKey(){
        return this.objectKey;
    }


}
