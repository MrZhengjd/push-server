package com.zheng.mq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

@Getter
@Setter
@ToString
@Slf4j

public abstract class DefaultConsumerConfigure implements MqConsumer {

    protected String topic;
    protected String tag;
    protected abstract void setTopic();
    protected abstract void setTag();
    protected DefaultMQPushConsumer consumer;

    protected abstract DefaultMQPushConsumer getDefaultMQPushConsumer() ;

    public DefaultConsumerConfigure() {
    }

    @Override
    public void consume() {
        try {
            setTopic();
            setTag();
            consumer = getDefaultMQPushConsumer();
            if (consumer == null){
                throw new IllegalArgumentException("consumer cannot start");
            }
            consumer.start();
            log.info("mq consumer start successfully");
        }catch (Exception e){
            log.error("start RocketMq exception",e);
            throw new IllegalArgumentException("start RocketMq exception");
        }
    }



    public abstract ConsumeConcurrentlyStatus dealBody(List<MessageExt> msgs);


}
