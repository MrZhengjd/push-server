package com.zheng.strategy;

import com.zheng.msg.AckMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class AckTaskQueue {
    private static ConcurrentLinkedQueue<AckMessage> ackQueue = new ConcurrentLinkedQueue<>();
    public static boolean pushAck(AckMessage ackMessage){
        return ackQueue.offer(ackMessage);
    }
    public static AckMessage getAck(){
        return ackQueue.poll();
    }
}
