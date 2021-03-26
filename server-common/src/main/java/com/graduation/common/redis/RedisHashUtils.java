package com.graduation.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisHashUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    //插入某个键值对
    @SuppressWarnings("unchecked")
    public void put(String key, String hashKey, Object hashValue){
        redisTemplate.opsForHash().put(key, hashKey, hashValue);
    }

    //插入键值对，默认时间单位为分
    public void put(String key, String hashKey, Object hashValue, Long expire){
        put(key, hashKey, hashValue, expire, TimeUnit.MINUTES);
    }

    //插入键值对，顺便设置过期时间
    @SuppressWarnings("unchecked")
    public void put(String key, String hashKey, Object hashValue, Long expire, TimeUnit timeUnit){
        put(key, hashKey, hashValue);
        redisTemplate.expire(key, expire, timeUnit);
    }

    //整个集合插入
    @SuppressWarnings("unchecked")
    public void put(String key, Map<String, Object> map){
        redisTemplate.opsForHash().putAll(key, map);
    }

    //插入集合，默认时间单位为分钟
    public void put(String key, Map<String, Object> map, Long expire){
        put(key, map, expire, TimeUnit.MINUTES);
    }

    //插入集合，顺便设置过期时间
    @SuppressWarnings("unchecked")
    public void put(String key, Map<String, Object> map, Long expire, TimeUnit timeUnit){
        put(key, map);
        redisTemplate.expire(key, expire, timeUnit);
    }

    //获取值
    @SuppressWarnings("unchecked")
    public Object get(String key, String hashKey){
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    //获取整个集合
    @SuppressWarnings("unchecked")
    public Map<String, Object> get(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    //删除
    @SuppressWarnings("unchecked")
    public void remove(String key){
        redisTemplate.delete(key);
    }

    //检查是否存在这个key
    @SuppressWarnings("unchecked")
    public Boolean contain(String key){
        return redisTemplate.hasKey(key);
    }

}
