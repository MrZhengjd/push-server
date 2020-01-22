package com.zheng.strategy;

import com.zheng.msg.BaseMessage;

public abstract class AbstractStrategy implements Strategy {


    public BaseMessage getBaseMessage() {
        return baseMessage;
    }

    protected BaseMessage baseMessage;

    public void setBaseMessage(BaseMessage baseMessage){
        this.baseMessage = baseMessage;
    }

    @Override
    public void dispatche() {

    }
}
