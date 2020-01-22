package com.zheng.request;

import com.zheng.msg.BaseMessage;
import com.zheng.netty.MessageType;

public interface SendRequest {

    /**
     * default type send
     */
     void send(BaseMessage message);

    /**
     * set type
     * @param type
     */
     void send(MessageType type,BaseMessage message);
}
