package com.graduation.common.redis.lock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
public @interface DistributeLock {
    //锁名字
    String name() default "";

    //锁自动释放时间
    long expire() default -1L;

    //锁自动释放单位
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
