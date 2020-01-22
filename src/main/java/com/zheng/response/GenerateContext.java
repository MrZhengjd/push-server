package com.zheng.response;

import com.zheng.msg.BaseMessage;
import com.zheng.msg.ResponseMessage;
import com.zheng.netty.MessageType;
import com.zheng.strategy.*;

import java.util.HashMap;
import java.util.Map;

public class GenerateContext {
    private static Map<MessageType, GenerateResponse> strategyMap = new HashMap<>();
    static {
        strategyMap.put(MessageType.ACK,new GenerateAckResponse());
        strategyMap.put(MessageType.PUSH,new GeneratePushResponse());
        strategyMap.put(MessageType.AUTH,new GenerateAuthResponse());
    }
    public ResponseMessage invokeStrategy(BaseMessage baseMessage,String information){
        GenerateResponse response = strategyMap.get(baseMessage.getMessageType());
        return response.generateByType(baseMessage,information);
    }
}
