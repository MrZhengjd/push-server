package com.zheng.server;

import com.zheng.msg.AckMessage;
import com.zheng.msg.ResponseMessage;
import com.zheng.netty.NettyChannelMap;
import com.zheng.response.GenerateContext;
import com.zheng.strategy.AckTaskQueue;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Callable;

@Slf4j
public class AckPullMessageController implements Callable<Void> {
    private GenerateContext context = new GenerateContext();

    private volatile boolean stopped = false;

    @Override
    public Void call() throws Exception {

        while (!stopped) {

            try {
                AckMessage message = AckTaskQueue.getAck();
                if (message == null) {
                    continue;
                }
                ResponseMessage responseMessage = context.invokeStrategy(message, null);
                if (StringUtils.isEmpty(message.getUid())){
                    throw new IllegalArgumentException("illegalarguement exception");
                }
                Channel channel = NettyChannelMap.get(message.getUid());
                if (channel != null)
                channel.writeAndFlush(responseMessage);
            } catch (Exception e) {
                log.error("send message error "+e);
            }
        }
        return null;
    }
}
