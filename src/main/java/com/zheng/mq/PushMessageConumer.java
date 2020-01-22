package com.zheng.mq;

import com.zheng.constants.Constants;
import com.zheng.msg.PushMessage;
import com.zheng.netty.DataSerialize;
import com.zheng.netty.DataSerializeFactory;
import com.zheng.server.AbstractServer;
import com.zheng.strategy.PushTaskQueue;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Getter
@Setter
@Component
public class PushMessageConumer extends DefaultConsumerConfigure  {

    private static PushMessageConumer pushMessageconsumer;
    @Autowired
    protected ConsumerConfig consumerConfig;
    @PostConstruct
    public void init(){
        pushMessageconsumer = this;
        pushMessageconsumer.consumerConfig = this.consumerConfig;
    }
    private final DataSerialize dataSerialize = DataSerializeFactory.getDefaultDataSerialize();

    @Override
    protected void setTopic() {
        this.topic = AbstractServer.serverId.toString();
    }

    @Override
    protected void setTag() {
        this.tag = Constants.TAG;
    }

    @Override
    protected DefaultMQPushConsumer getDefaultMQPushConsumer(){
        try {
            System.out.println("mq listen to the message " + topic);
            return defaultMQPushConsumer();

        }catch (Exception e){
            log.error("error "+e,e);
        }
        return null;
    }
    public DefaultMQPushConsumer defaultMQPushConsumer(){
        try {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(pushMessageconsumer.consumerConfig.groupName);
            consumer.setNamesrvAddr(pushMessageconsumer.consumerConfig.namesrvAddr);
            consumer.subscribe(topic, tag);
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    return dealBody(msgs);
                }
            });
            log.info("consumer start ----------"+consumer);
            return consumer;
        }catch (Exception e){
            log.error("start consumer error "+e,e);
        }
        return null;
    }
    @Override
    public ConsumeConcurrentlyStatus dealBody(List<MessageExt> msgs) {
        log.info("consumer start receive message");
        try {
            for (MessageExt messageExt:msgs
                 ) {
                PushMessage pushMessage = dataSerialize.deserialize(messageExt.getBody(),PushMessage.class);
                PushTaskQueue.pushPushMessage(pushMessage);
                log.info("mq receive message" + pushMessage);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            log.error("error on consumer "+ e);
        }
        return null;
    }


    public PushMessageConumer() {

    }

}
