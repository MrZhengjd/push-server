package com.zheng.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

public class ServerInitilizer extends ChannelInitializer {
    public String getServerPath() {
        return serverPath;
    }


    public static String serverPath;
    private final EventExecutorGroup group;

    public ServerInitilizer(EventExecutorGroup group,String serverPath) {
        this.group = group;
        this.serverPath = serverPath;
    }


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast("ping",new IdleStateHandler(60,0,0, TimeUnit.SECONDS))
                .addLast("encoder", new MessageEncoder())
                .addLast("decoder", new MessageDecoder())
                .addLast("auth",new AuthHandler())
                .addLast(group,"handler",new ServerHandler())
                ;
    }
}
