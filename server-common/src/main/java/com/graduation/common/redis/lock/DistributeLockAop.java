package com.graduation.common.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//分布式锁注解拦截
@Slf4j
@Aspect
@Component
public class DistributeLockAop {
    @Autowired
    private DistributedLocker distributedLocker;

    @Around("@annotation(distributeLock)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributeLock distributeLock) throws Throwable{
        //获取锁名
        String name = distributeLock.name();
        if(StringUtils.isBlank(name)){
            name = "lock_" + joinPoint.getSignature().getName();
            System.out.println(name);
        }

        //设置超时时间
        long expire = distributeLock.expire();

        if(expire == -1L){
            distributedLocker.lock(name);
        }else{
            distributedLocker.lock(name, distributeLock.expire(), distributeLock.timeUnit());
        }

        //执行原方法
        Object result = joinPoint.proceed();

        //解锁
        distributedLocker.unlock(name);

        return result;
    }
}
