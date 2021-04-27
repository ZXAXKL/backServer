package com.graduation.device.service.rabbitmq.log;

import com.alibaba.fastjson.JSONObject;
import com.graduation.common.rabbitmq.producer.Producer;
import org.springframework.stereotype.Component;

@Component
public class OperationLogProducer extends Producer {

    public void send(Integer userId, String info){
        //简单打包一下异常设备的信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomId", userId);
        jsonObject.put("info", info);
        //投递到消息队列
        sendDirectMsg("log.user.operation", jsonObject);
    }
}