package com.zheng.mq;

public interface MqProduce {

    void start();
    void send(Object message);
}
