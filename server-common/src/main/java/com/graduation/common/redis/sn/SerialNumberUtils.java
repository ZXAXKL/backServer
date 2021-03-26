package com.graduation.common.redis.sn;

import com.graduation.common.date.DateUtils;
import com.graduation.common.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SerialNumberUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    public String incrAndGetFullSn(String key){
        key = key + "_sn-" + DateUtils.formatDate(new Date(),  "yyyyMMdd");
        return key + incr(key);
    }

    public String incrAndGetSnStr(String key){
        key = key + "_sn-" + DateUtils.formatDate(new Date(),  "yyyyMMdd");
        return String.valueOf(incr(key));
    }

    public Long incrAndGetSn(String key){
        key = key + "_sn-" + DateUtils.formatDate(new Date(),  "yyyyMMdd");
        return incr(key);
    }

    public Long getSn(String key){
        key = key + "_sn-" + DateUtils.formatDate(new Date(),  "yyyyMMdd");
        Object sn = RedisUtils.get(key);
        return sn == null ? null : Long.parseLong(sn.toString());
    }

    private long incr(String key){
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        //执行获取自增号并执行自增操作
        long increment = entityIdCounter.getAndIncrement();

        //初始设置过期时间
        if (increment == 0) {
            entityIdCounter.expire(DateUtils.getSecondsNextEarlyMorning(), TimeUnit.SECONDS);
        }

        return increment;
    }

}
