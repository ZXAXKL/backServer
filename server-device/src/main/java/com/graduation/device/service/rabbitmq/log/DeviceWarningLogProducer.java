package com.graduation.device.service.rabbitmq.log;

import com.alibaba.fastjson.JSONObject;
import com.graduation.common.rabbitmq.producer.ReliableProducer;
import org.springframework.stereotype.Component;

@Component
public class DeviceWarningLogProducer extends ReliableProducer {

    @Override
    protected Integer limit() {
        return 10;
    }

    @Override
    protected Integer interval() {
        return 5;
    }

    public void send(String sn, JSONObject info){
        //简单打包一下异常设备的信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sn", sn);
        jsonObject.put("info", info);
        //投递到消息队列
        sendDirectMsg("log.device.warn", jsonObject, serialNumberUtils.incrAndGetFullSn("log_device_warn"));
    }

}
