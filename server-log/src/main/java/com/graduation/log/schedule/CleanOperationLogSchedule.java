package com.graduation.log.schedule;

import com.graduation.common.schedule.TimedTask;
import com.graduation.log.service.log.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CleanOperationLogSchedule extends TimedTask {

    @Autowired
    private OperationLogService operationLogService;

    @Override
    protected String name() {
        return "operation_log_clean";
    }

    @Override
    protected String cron() {
        //每天十二点触发一次
        return "0 0 12 * * ?";
    }

    @Override
    public void task() throws Exception {
        operationLogService.clean(15);
    }

    @Override
    protected Long leaseTime() {
        //加锁1000ms保证任务执行完成
        return 2000L;
    }

    @Override
    protected Long sleepTime() {
        //100ms轮询一次
        return 100L;
    }

}

