package com.zheng.strategy;

import com.zheng.netty.MessageType;
import com.zheng.msg.BaseMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class StrategyContext {

    private static Map<MessageType,AbstractStrategy> strategyMap = new HashMap<>();
    static {
        strategyMap.put(MessageType.PING,new PingStrategy());
        strategyMap.put(MessageType.PONG,new PongStrategy());
        strategyMap.put(MessageType.SEND,new SendStrategy());
        strategyMap.put(MessageType.ACK,new AckStrategy());
        strategyMap.put(MessageType.PUSH,new PushStrategy());
    }

    public void invokeStrategy(BaseMessage baseMessage){
        try {
            AbstractStrategy strategy = strategyMap.get(baseMessage.getMessageType());
            if (strategy == null){
                throw new IllegalArgumentException("new instance error");
            }
            strategy.setBaseMessage(baseMessage);
            baseMessage.setRequestId(generateRequestId());
            strategy.dispatche();
        }catch (Exception e){
            log.error("find strate error ",e);
        }

    }
    private String generateRequestId(){
        String requestId = UUID.randomUUID().toString();
        requestId = requestId.replace("-","");
        return requestId;
    }
}
