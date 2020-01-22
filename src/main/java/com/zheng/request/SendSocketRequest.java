package com.zheng.request;

import com.zheng.msg.BaseMessage;
import com.zheng.msg.Header;
import com.zheng.msg.RequestMessage;
import com.zheng.netty.MessageType;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;


public class SendSocketRequest implements SendRequest {
    private AtomicLong msgId = new AtomicLong(0L);

    private final SocketChannel socketChannel;
    public Map<String, CallBackInvoker<Object>> invokeMap = new ConcurrentSkipListMap<String, CallBackInvoker<Object>>();

    public SendSocketRequest(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void send(BaseMessage message) {
        send(MessageType.SEND,message);
    }

    @Override
    public void send(MessageType type,BaseMessage message) {
        if (socketChannel == null){
            throw new NullPointerException("Null Socket channel can't send message");
        }
        Header header = new Header(type.value);
        message.setMessageId(String.valueOf(msgId.getAndIncrement()));
        RequestMessage request = new RequestMessage();
        request.setMessage(message);

        request.setHeader(header);
        ChannelFuture channelFuture = socketChannel.writeAndFlush(request);
        CallBackInvoker<Object> invoker = new CallBackInvoker<>();
        invoker.join(new CallBackListener<Object>() {
            @Override
            public void onCallBack(Object o) {

                System.out.println("receive message "+ o);

            }
        });
        invokeMap.put(message.getMessageId(),invoker);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if ( ! future.isSuccess()){
                    invoker.setReason(future.cause());
                }
            }
        });

    }
    public boolean trace(String key) {
        return invokeMap.containsKey(key);
    }

    public CallBackInvoker<Object> detach(String key) {
        if (invokeMap.containsKey(key)) {
            return invokeMap.remove(key);
        } else {
            return null;
        }
    }
}
