package com.graduation.log.logic.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.graduation.common.rabbitmq.consumer.direct.DirectConsumer;
import com.graduation.log.service.log.OperationLogService;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OperationLogConsumer extends DirectConsumer {

    @Autowired
    private OperationLogService operationLogService;

    @Override
    public String key() {
        return "log.user.operation";
    }

    @Override
    public void consume(JSONObject payload, MessageProperties properties) {
        operationLogService.record(payload.getInteger("roomId"), payload.getString("info"), properties.getTimestamp());
    }
}
