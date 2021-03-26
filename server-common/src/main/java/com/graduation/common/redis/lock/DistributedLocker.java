package com.graduation.common.redis.lock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public interface DistributedLocker {

    RLock lock(String lockKey);

    RLock lock(String lockKey, long expire);

    RLock lock(String lockKey, long expire, TimeUnit unit);

    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit);

    boolean tryLock(String lockKey, long waitTime, long leaseTime);

    boolean tryLock(String lockKey, long waitTime);

    void unlock(String lockKey);

    void unlock(RLock lock);

}

