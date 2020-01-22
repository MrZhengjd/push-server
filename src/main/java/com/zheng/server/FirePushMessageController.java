package com.zheng.server;

import com.zheng.msg.PushMessage;
import com.zheng.msg.ResponseMessage;
import com.zheng.netty.NettyChannelMap;
import com.zheng.response.GenerateContext;
import com.zheng.strategy.PushTaskQueue;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class FirePushMessageController implements Callable<Void> {
    private GenerateContext context = new GenerateContext();

    private volatile boolean stopped = false;

    @Override
    public Void call() throws Exception {

        while (!stopped) {

            try {
                PushMessage message = PushTaskQueue.getPush();
                if (message != null) {
                    ResponseMessage responseMessage = context.invokeStrategy(message, null);
                    Channel channel = NettyChannelMap.get(message.getToUser());
                    if (channel == null){
                        storeInSql();
                        log.info("destinate user offline "+message.getToUser());
                        continue;
                    }
                    channel.writeAndFlush(responseMessage);

                }
            } catch (Exception e) {
                log.error("send message error "+e);
            }
        }
        return null;
    }
    public void storeInSql(){

    }
}
