package com.graduation.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@AutoConfigureOrder(value = 1)
public class RedisUtils {

    private static RedisUtils redisUtils;

    private DefaultRedisScript<String> getAndRefresh;

    private DefaultRedisScript<String> getAndRemove;

    private DefaultRedisScript<String> setnxAndExpire;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        redisUtils = this;
        String getAndRefreshLua =
                "local result = redis.call(\"get\", KEYS[1]) \n" +
                "if result ~= nil then\n" +
                "redis.call(\"pexpire\", KEYS[1], ARGV[1])\n" +
                "end\n" +
                "return result";
        getAndRefresh = new DefaultRedisScript<>(getAndRefreshLua, String.class);

        String getAndRemoveLua =
                "local result = redis.call(\"get\", KEYS[1])\n" +
                "if result ~= nil then\n" +
                "redis.call(\"del\", KEYS[1])\n" +
                "end\n" +
                "return result";

        getAndRemove = new DefaultRedisScript<>(getAndRemoveLua, String.class);

        String setnxAndExpireLua =
                "local result = redis.call(\"get\", KEYS[1])\n" +
                "if not result then\n" +
                "redis.call(\"set\", KEYS[1], ARGV[1])\n" +
                "redis.call(\"pexpire\", KEYS[1], ARGV[2])\n" +
                "end\n" +
                "return result";

        setnxAndExpire = new DefaultRedisScript<>(setnxAndExpireLua, String.class);
    }



    public static void put(String key, Object value, Long expire){
        put(key, value, expire, TimeUnit.MINUTES);
    }

    @SuppressWarnings("unchecked")
    public static void put(String key, Object value, Long expire, TimeUnit timeUnit){
        redisUtils.redisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }

    @SuppressWarnings("unchecked")
    public static long getExpire(String key, TimeUnit timeUnit){
        return redisUtils.redisTemplate.getExpire(key, timeUnit);
    }

    @SuppressWarnings("unchecked")
    public static void put(String key, Object value){
        redisUtils.redisTemplate.opsForValue().set(key, value);
    }

    public static Object get(String key){
        return redisUtils.redisTemplate.opsForValue().get(key);
    }

    public static Object getAndRefresh(String key, Long expire){
        return getAndRefresh(key, expire, TimeUnit.MINUTES);
    }

    @SuppressWarnings("unchecked")
    public static Object getAndRefresh(String key, Long expire, TimeUnit timeUnit){
        return redisUtils.redisTemplate.execute(
                redisUtils.getAndRefresh,
                redisUtils.redisTemplate.getKeySerializer(),
                redisUtils.redisTemplate.getValueSerializer(),
                Collections.singletonList(key),
                String.valueOf(TimeoutUtils.toMillis(expire, timeUnit))
        );
    }

    @SuppressWarnings("unchecked")
    public static Object getAndRemove(String key){
        return redisUtils.redisTemplate.execute(
                redisUtils.getAndRemove,
                redisUtils.redisTemplate.getKeySerializer(),
                redisUtils.redisTemplate.getValueSerializer(),
                Collections.singletonList(key)
        );
    }

    @SuppressWarnings("unchecked")
    public static void remove(String key){
        redisUtils.redisTemplate.delete(key);
    }

    public static Boolean setnxAndExpire(String key, Object value){
        return setnxAndExpire(key, value, 20L, TimeUnit.MINUTES);
    }

    @SuppressWarnings("unchecked")
    public static Boolean setnxAndExpire(String key, Object value, Long expire, TimeUnit timeUnit){
        return redisUtils.redisTemplate.execute(
                redisUtils.setnxAndExpire,
                redisUtils.redisTemplate.getKeySerializer(),
                redisUtils.redisTemplate.getValueSerializer(),
                Collections.singletonList(key),
                value.toString(),
                String.valueOf(TimeoutUtils.toMillis(expire, timeUnit))
        ) == null;
    }

}
