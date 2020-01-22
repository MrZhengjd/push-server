package com.zheng.msg;

import com.zheng.netty.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PushMessage extends BaseMessage {
    private String fromUser;
    private String toUser;
    private Object message;
    public PushMessage() {

        super(MessageType.PUSH);
    }

    public PushMessage( String fromUser, String toUser, Object message) {
        super(MessageType.PUSH);
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.message = message;
    }


}
