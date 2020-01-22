package com.zheng.netty;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyChannelMap {
    private static Map<String, Channel> map = new ConcurrentHashMap<>();

    public static void add(String clientId, Channel socketChannel) {
        map.put(clientId, socketChannel);
    }

    public static Channel get(String clientId) {
        return map.get(clientId);
    }

    public static void remove(Channel socketChannel) {
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue() == socketChannel) {
                map.remove(entry.getKey());
            }
        }
    }
    public static void deleteByClientId(String clientId){
        if (StringUtils.isEmpty(clientId)){
            return;
        }
        map.remove(clientId);
    }
    public static String contain(Channel channel){

        for (Map.Entry entry : map.entrySet()){
            if (entry.getValue() == channel){
                return (String) entry.getKey();
            }
        }
        return null;
    }

}
