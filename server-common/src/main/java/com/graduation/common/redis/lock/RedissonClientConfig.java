package com.graduation.common.redis.lock;

import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonClientConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient getRedisson(){
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer().setAddress("redis://" + host + ":" + port);
        if(!StringUtils.isBlank(password)){
            singleServerConfig.setPassword("123456");
        }
        return Redisson.create(config);
    }
}

