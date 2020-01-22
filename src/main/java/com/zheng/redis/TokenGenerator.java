package com.zheng.redis;

public interface TokenGenerator {

    <T> String create(T obj, long maxAge);

    <T> T unsign(String jwt, Class<T> clazz);
}
