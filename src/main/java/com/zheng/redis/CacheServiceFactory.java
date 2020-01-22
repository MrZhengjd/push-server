package com.zheng.redis;

public class CacheServiceFactory {

     private CacheServiceFactory(){}
     static class InstanceHolder{
         static CacheServiceFactory serviceFactory = new CacheServiceFactory();
     }
     public static CacheServiceFactory getInstance(){
         return InstanceHolder.serviceFactory;
     }

     public static CacheService getRediscacheService(){
         return new RedisLoginServiceImpl();
     }
}
