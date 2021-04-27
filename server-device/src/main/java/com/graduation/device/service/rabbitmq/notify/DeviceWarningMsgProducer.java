package com.graduation.device.service.rabbitmq.notify;

import com.alibaba.fastjson.JSONObject;
import com.graduation.common.rabbitmq.producer.Producer;
import org.springframework.stereotype.Component;

@Component
public class DeviceWarningMsgProducer extends Producer {

    public void send(String sn, String error){
        //简单打包一下异常设备的信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sn", sn);
        jsonObject.put("error", error);
        //投递到消息队列
        sendDirectMsg("msg.device.error", jsonObject);
    }

}
