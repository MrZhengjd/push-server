package com.zheng.redis;


import org.springframework.stereotype.Service;

@Service("jwtTokengenerator")
public class JWTTokenGenerator implements TokenGenerator {

    @Override
    public <T> String create(T obj, long maxAge) {
        String value = JWT.sign(obj,maxAge);
        return value;
    }

    @Override
    public <T> T unsign(String jwt,Class<T> clazz) {
        return JWT.unsign(jwt, clazz);

    }
}
