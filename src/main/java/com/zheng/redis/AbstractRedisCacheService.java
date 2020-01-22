package com.zheng.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public abstract class AbstractRedisCacheService<T extends AbstractKey> implements CacheService<T> {

    protected abstract RedisTemplate getTemplate();
    @Override
    public void put(T value) {
        try {
            HashOperations hashOperations = getTemplate().opsForHash();
            String string = new ObjectMapper().writeValueAsString(value);
            hashOperations.put(value.getObjectKey(), value.getKey(), string);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void delete(T value) {
        try {
            getTemplate().opsForHash().delete(value.getObjectKey(),value.getKey());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public T get(T key,Class<T> clazz) {
        try {

            HashOperations hashOperations = getTemplate().opsForHash();
            String json = (String) hashOperations.get(key.getObjectKey(), key.getKey());
            ObjectMapper objectMapper = new ObjectMapper();
            T abstractKey =  objectMapper.readValue(json,clazz);
            return abstractKey;
        }catch (Exception e){
           log.error("ops exception " + e,e);
            return null;
        }

    }
}
