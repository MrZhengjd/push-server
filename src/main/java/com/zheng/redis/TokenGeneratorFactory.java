package com.zheng.redis;

public class TokenGeneratorFactory {

    private TokenGeneratorFactory(){}
    private static class InstanceHolder{
        static TokenGeneratorFactory instance = new TokenGeneratorFactory();
    }
    public static TokenGeneratorFactory getInstance(){
        return InstanceHolder.instance;
    }
    public static TokenGenerator getDefaultTokenGenerator(){
        return new JWTTokenGenerator();
    }
}
