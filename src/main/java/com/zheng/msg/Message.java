package com.zheng.msg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message {

    private Header header;
    private BaseMessage message;

    public Message() {
    }

    public Message(Header header, BaseMessage body) {
        this.header = header;
        this.message = body;
    }
}
