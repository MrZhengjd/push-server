package com.zheng.loadbalance;

public interface RegistProvider {
    void regist(Object context) throws Exception;
    void unRegist(Object context)throws Exception;
}
