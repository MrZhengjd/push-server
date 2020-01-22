package com.zheng.mq;

public class MqProduceFactory {

    private MqProduceFactory() {
    }

    private static class InstanceHolder{
        static MqProduceFactory instance = new MqProduceFactory();
    }
    public static MqProduceFactory getInstance(){
        return InstanceHolder.instance;
    }

    public static MqProduce getDefaultMqProduce(){
        return new DefaultMqProduce();
    }
}
