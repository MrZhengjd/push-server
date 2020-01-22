package com.zheng.strategy;

import com.zheng.msg.PushMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PushTaskQueue {
    private static ConcurrentLinkedQueue<PushMessage> ackQueue = new ConcurrentLinkedQueue<>();
    public static boolean pushPushMessage(PushMessage pushMessage){
        return ackQueue.offer(pushMessage);
    }
    public static PushMessage getPush(){
        return ackQueue.poll();
    }
}
