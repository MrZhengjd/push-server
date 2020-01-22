package com.zheng.mq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration("defaultMqProduce")
@Getter
@Setter
@ToString
@Slf4j
@PropertySource(value = "classpath:rocketmq.properties")
public class DefaultMqProduce implements MqProduce {
    //    private DefaultMqProduce mqProduce;
    @Value("${rocketmq.producer.namesvrAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.producer.groupName}")
    private String groupName;
    @Value("${rocketmq.producer.resendtime}")
    private int resendTime;

    private DefaultMQProducer defaultProducer;

    public DefaultMqProduce() {
    }
//    @PostConstruct
//    public void init(){
//        mqProduce = this;
//        mqProduce.defaultProducer = this.defaultProducer;
//    }

    public DefaultMQProducer defaultProducer() throws MQClientException {
        try {
            DefaultMQProducer producer = new DefaultMQProducer(groupName);
            producer.setNamesrvAddr(namesrvAddr);
            producer.setVipChannelEnabled(false);
            producer.setRetryTimesWhenSendAsyncFailed(resendTime);
            log.info("start mq produce");
            log.info("rocketmq producer server开启成功---------------------------------.");
            return producer;
        } catch (Exception e) {
            log.error("start mq error " + e);
        }
        return null;
    }

    @Override
    public void start() {
        try {
            defaultProducer = defaultProducer();
            if (defaultProducer == null){
                log.error("null point exception ");
                throw new NullPointerException("DefaultMQProducer null");
            }
            defaultProducer.start();
            log.info("rocketmq producer server开启成功---------------------------------.");
        } catch (Exception e) {
            log.error("start mq error " + e);
        }
    }

    @Override
    public void send(Object message) {
        try {
            Message mqMessage = (Message) message;
            defaultProducer.send(mqMessage, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("mq send message " + sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("mq transaction error " + throwable);
                }
            });
        } catch (InterruptedException e) {
            log.error("interrpted exception ", e);
        } catch (RemotingException e) {
            log.error("remotingException ", e);
        } catch (MQClientException e) {
            log.error("mqclientException ", e);
        }

    }
}
