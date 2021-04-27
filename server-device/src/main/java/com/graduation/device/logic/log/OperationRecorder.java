package com.graduation.device.logic.log;

import com.graduation.device.service.rabbitmq.log.OperationLogProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public abstract class OperationRecorder {

    @Autowired
    private OperationLogProducer operationLogProducer;

    //表明该类处理的日志类型
    public abstract String type();

    //子类实现生成日志的方法，下面三个参数分别为，用户昵称、参数列表以及操作结果
    public abstract String execute(String name, Map<String, Object> properties);

    //投递
    public final void delivery(Integer roomId, String log){
        operationLogProducer.send(roomId, log);
    }

}
