package com.zheng.msg;

import com.zheng.netty.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public  class BussinessMessage extends BaseMessage{

    private String RequestId;

    protected BussinessMessage(MessageType messageType) {
        super(messageType);
    }
}
