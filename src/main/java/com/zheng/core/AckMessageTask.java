package com.zheng.core;

import com.google.common.base.Splitter;
import com.zheng.msg.AckMessage;
import com.zheng.msg.BaseMessage;
import com.zheng.msg.ResponseVo;
import com.zheng.strategy.AckTaskQueue;

import java.util.Spliterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

public class AckMessageTask implements Callable<Long> {
    public final static String MessageDelimiter = "@";
    public static final String SystemPropertyAckTaskSemaphoreValue ="Ack";
    private final AtomicLong count = new AtomicLong(0);
    private CyclicBarrier barrier = null;
    private String[] messages = null;
    public AckMessageTask(CyclicBarrier barrier, String[] messages) {
        this.barrier = barrier;
        this.messages = messages;
    }

    @Override
    public Long call() throws Exception {

        for (int i = 0;i<messages.length;i++){
            boolean error = false;
            ResponseVo responseVo = null;
            AckMessage ackMessage = new AckMessage();

            Object[] msg = Splitter.on(MessageDelimiter).trimResults().splitToList(messages[i]).toArray();
            if (msg.length == 3){
                ackMessage.setRequestId((String) msg[0]);
                ackMessage.setMessageId((String) msg[1]);
                ackMessage.setUid((String) msg[2]);
            }
            if (error){
                responseVo = ResponseVo.fail("error ");
            }
            else {
                responseVo = ResponseVo.success("success");
                count.incrementAndGet();
            }
            ackMessage.setResponseVo(responseVo);
            AckTaskQueue.pushAck(ackMessage);
            SemaphoreCache.release(SystemPropertyAckTaskSemaphoreValue);

        }
        barrier.await();
        return count.get();
    }
}
