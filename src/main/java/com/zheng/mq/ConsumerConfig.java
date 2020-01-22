package com.zheng.mq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@PropertySource(value = "classpath:rocketmq.properties")
@Configuration
@ToString
public class ConsumerConfig {
    @Value("${rocketmq.consumer.namesrvAddr}")
    protected String namesrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    protected String groupName;
    @Value("${rocketmq.consumer.resendtime}")
    protected int resendTime;
}
