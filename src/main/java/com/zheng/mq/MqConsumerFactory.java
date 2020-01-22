package com.zheng.mq;

public class MqConsumerFactory {

    private MqConsumerFactory() {
    }

    private static class InstanceHolder{
        static MqConsumerFactory instance = new MqConsumerFactory();
    }
    public static MqConsumerFactory getInstance(){
        return InstanceHolder.instance;
    }

    public static MqConsumer getDefaultMqConsumer(){
        return new PushMessageConumer();
    }
}
