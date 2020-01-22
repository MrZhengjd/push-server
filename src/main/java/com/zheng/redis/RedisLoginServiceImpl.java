package com.zheng.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("redisLoginService")
@Getter
@Setter
public class RedisLoginServiceImpl extends AbstractRedisCacheService<RedisLogin> {

    private static RedisLoginServiceImpl redisLoginService;
    @Autowired
    public RedisTemplate template ;

    @PostConstruct
    public void init(){
        redisLoginService = this;
        redisLoginService.template = this.template;
    }
    @Override
    public RedisTemplate getTemplate() {
        return redisLoginService.template;
    }


    public RedisLoginServiceImpl() {

    }
}
