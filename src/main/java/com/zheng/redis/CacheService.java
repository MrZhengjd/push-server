package com.zheng.redis;

public interface CacheService<T> {
    void put(T value);
    void delete(T value);
    T get(T key, Class<T> clazz);
}
