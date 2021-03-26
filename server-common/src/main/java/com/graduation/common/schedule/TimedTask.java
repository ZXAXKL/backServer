package com.graduation.common.schedule;

import com.graduation.common.redis.lock.DistributedLocker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.TimeUnit;

public abstract class TimedTask implements SchedulingConfigurer {

    @Autowired
    private DistributedLocker distributedLocker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    //任务名
    protected abstract String name();

    //cron表达式
    protected abstract String cron();

    //任务内容
    public abstract void task() throws Exception;

    //锁自动释放时间，要大于任务执行时间
    protected abstract Long leaseTime();

    //轮询休息时间
    protected abstract Long sleepTime();

    @Override
    @SuppressWarnings("unchecked")
    public void configureTasks(@NonNull ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addCronTask(() -> {
            String sign = "timed_task_" + name() + "_sign";
            String lockName = "timed_task_" + name() + "_lock";
            //判断标记是否存在，如果不存在说明定时任务没有完成
            while(redisTemplate.opsForValue().get(sign) == null){
                //尝试获取分布式锁
                if(distributedLocker.tryLock(lockName, 0, leaseTime())){
                    //获取成功之后还要再判断一次标记是否存在，因为期间可能有实例完成了定时任务并释放了锁
                    if(redisTemplate.opsForValue().get(sign) == null){
                        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
                        //设置事务的传播机制
                        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
                        //得到事务。根据事务的传播机制判断是新建事务还是用当前已有的事务
                        TransactionStatus transStatus = platformTransactionManager.getTransaction(transDefinition);
                        try {
                            //执行定时任务
                            task();
                            //完成任务后设置完成标记,这个时间为20倍的轮询时间，保证其他实例全部退出
                            redisTemplate.opsForValue().set(sign, "", sleepTime() >= 50 ? sleepTime() / 50 : 1, TimeUnit.SECONDS);
                            //释放锁
                            distributedLocker.unlock(lockName);
                            // 提交事务
                            platformTransactionManager.commit(transStatus);
                        } catch (Exception e) {
                            // 回滚事务
                            platformTransactionManager.rollback(transStatus);
                        }
                    }
                    //无论是执行完毕还是检查到标记都需要结束
                    break;
                }else {
                    //没有获取到锁，则等待一段时间
                    try {
                        TimeUnit.MILLISECONDS.sleep(sleepTime());
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }, cron());
    }

}
