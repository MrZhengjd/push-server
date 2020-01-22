package com.zheng.strategy;

import com.google.common.base.Joiner;
import com.zheng.constants.Constants;
import com.zheng.core.AckMessageCache;
import com.zheng.mq.MqProduce;
import com.zheng.mq.MqProduceFactory;
import com.zheng.msg.PushMessage;
import com.zheng.msg.SendMessage;
import com.zheng.netty.DataSerialize;
import com.zheng.netty.DataSerializeFactory;
import com.zheng.netty.DistributeSession;
import com.zheng.redis.CacheService;
import com.zheng.redis.CacheServiceFactory;
import com.zheng.server.AbstractServer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;


@Getter
@Setter
@Slf4j
@Component
public class SendStrategy extends AbstractStrategy {
    private static AtomicLong messageCount = new AtomicLong(0);

    public final static String MessageDelimiter = "@";

    private static SendStrategy sendStrategy;
    private CacheService cacheService = CacheServiceFactory.getRediscacheService();
    @Autowired
    @Qualifier("defaultMqProduce")
    protected MqProduce mqProduce;
    private DataSerialize dataSerialize = DataSerializeFactory.getDefaultDataSerialize();
    @PostConstruct
    public void init(){
        sendStrategy = this;
        sendStrategy.mqProduce = this.mqProduce;
    }

    public SendStrategy() {

    }

    @Override
    public void dispatche() {
        try {
            SendMessage sendMessage = (SendMessage) baseMessage;
            taskAdd();
            PushMessage pushMessage = new PushMessage(sendMessage.getFromUser(), sendMessage.getToUser(), sendMessage.getMessage());
            sendPushMessage(pushMessage);
        } catch (Exception e) {
            log.error("handle send message catch error " + e,e);
        }

    }

    private void taskAdd() {
        try {
            Joiner joiner = Joiner.on(MessageDelimiter).skipNulls();
            String key = joiner.join(baseMessage.getRequestId(), baseMessage.getMessageId(), baseMessage.getUid());
            AckMessageCache.getInstance().appednMessage(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPushMessage(PushMessage pushMessage) throws Exception {
        byte[] byteMessage = dataSerialize.serialize(pushMessage);
        log.info("send message to mq count " + messageCount.getAndIncrement());
        DistributeSession session = (DistributeSession) cacheService.get(DistributeSession.build(null, pushMessage.getToUser()), DistributeSession.class);
        if (session == null){
            writeInDataBase();
            return;
        }
        Message message = new Message(String.valueOf(session.getServerId()), Constants.TAG, byteMessage);
        sendStrategy.mqProduce.send(message);
    }
    public void writeInDataBase(){
        log.info("data have write in database");
    }
}
