package com.zheng.loadbalance;

public class IdGeneratorFactory {

    static class InstanceHolder{
        static IdGeneratorFactory instance = new IdGeneratorFactory();
    }
    public static IdGeneratorFactory getInstance(){
        return InstanceHolder.instance;
    }

    public static IdGenerator getDefaultGenerator(){
        return new SnowflakeIdGenerator();
    }
}
