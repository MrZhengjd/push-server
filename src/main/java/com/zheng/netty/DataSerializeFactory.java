package com.zheng.netty;

import com.zheng.mq.MqConsumer;
import com.zheng.mq.PushMessageConumer;

public class DataSerializeFactory {

    private DataSerializeFactory() {
    }

    private static class InstanceHolder{
        static DataSerializeFactory instance = new DataSerializeFactory();
    }
    public static DataSerializeFactory getInstance(){
        return InstanceHolder.instance;
    }

    public static DataSerialize getDefaultDataSerialize(){
        return new PbSerializeUtil();
    }
}
