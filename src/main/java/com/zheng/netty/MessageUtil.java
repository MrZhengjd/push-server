package com.zheng.netty;

import com.zheng.msg.*;

import java.util.HashMap;
import java.util.Map;

public class MessageUtil {
    private static Map<MessageType, BaseMessage> messageMap = new HashMap<>();
    static {
        messageMap.put(MessageType.PING, new PingMessage());
        messageMap.put(MessageType.PONG, new PongMessage());
        messageMap.put(MessageType.ACK, new AckMessage());
        messageMap.put(MessageType.PUSH,new PushMessage());
        messageMap.put(MessageType.SEND,new SendMessage());
    }
    public static BaseMessage getMessageByType(byte type){
        MessageType messageType = MessageType.fromName(type);
        if (messageType != null){
            return  messageMap.get(messageType);
        }
        return null;

    }
}
