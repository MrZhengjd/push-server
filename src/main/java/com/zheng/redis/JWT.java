package com.zheng.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.HashMap;
import java.util.Map;

public class JWT {
    private static final String SECRET = "HONGHUJWT1234567890QWERTYUIOPASDFGHJKLZXCVBNM";

    private static final String EXP = "exp";

    private static final String PAYLOAD = "payload";
    private static final String ISSUER = "issuer"; //签发人
    //加密
    public static <T> String sign(T object, long maxAge) {
        try {
            final Map<String, Object> claims = new HashMap<String, Object>();
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(object);
            claims.put(PAYLOAD, jsonString);
            claims.put(EXP, System.currentTimeMillis() + maxAge);
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            //添加构成JWT的参数
            JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                    .setPayload(mapper.writeValueAsString(claims))
                    .signWith(signatureAlgorithm,SECRET.getBytes()); //估计是第三段密钥
            //生成JWT
            return builder.compact();

        } catch (Exception e) {
            return null;
        }
    }

    //解密
    public static <T> T unsign(String jwt, Class<T> classT) {
        try {
            final Map<String, Object> claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(jwt).getBody();

            if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {
                String json = (String) claims.get(PAYLOAD);
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(json, classT);

            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
