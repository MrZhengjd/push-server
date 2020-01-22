package com.zheng.request;

import com.zheng.executor.MyExecutor;
import com.zheng.msg.BaseMessage;
import com.zheng.netty.MessageType;
import io.netty.channel.socket.SocketChannel;


public class RequestProxy implements SendRequest {

    private final SendSocketRequest socketRequest;

    public RequestProxy(SocketChannel channel) {
        this.socketRequest = new SendSocketRequest(channel);
    }

    @Override
    public void send(BaseMessage message) {

        MyExecutor.submit(()->{
            socketRequest.send(message);
        });
    }

    @Override
    public void send(MessageType type, BaseMessage message) {
        MyExecutor.submit(()->{
            socketRequest.send(type,message);
        });
    }
}
