package com.graduation.log.logic.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.graduation.common.rabbitmq.consumer.direct.DirectConsumer;
import com.graduation.log.service.log.DeviceWarnLogService;
import com.graduation.log.table.DeviceWarnLog;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Component
public class DeviceWarnLogConsumer extends DirectConsumer {

    @Autowired
    private DeviceWarnLogService deviceWarnLogService;

    @Override
    public String key(){
        return "log.device.warn";
    }

    @Override
    public void consume(JSONObject payload, MessageProperties properties) {
        //除去消息序列号中用于区分不同序列号的前缀
        //取得日期+流水号并转换为Long类型，当作日志id
        System.out.println(payload);
        String[] strs = properties.getCorrelationId().split("-");
        Long msgSn = Long.parseLong(strs[strs.length - 1]);
        //填写日志信息
        DeviceWarnLog deviceWarnLog = new DeviceWarnLog();
        //日志id
        deviceWarnLog.setId(msgSn);
        //用户id
        deviceWarnLog.setRoomId(payload.getInteger("roomId"));
        //设备别名
        deviceWarnLog.setConfirmId(payload.getInteger("confirmId"));
        //设备序列号
        deviceWarnLog.setSn(payload.getString("sn"));
        //警告内容
        String info = payload.getString("info");
        deviceWarnLog.setInfo(info == null ? "" : info);
        //把消息时间戳当作日志的时间戳
        deviceWarnLog.setCreateDate(properties.getTimestamp());

        try{
            //插入数据库
            deviceWarnLogService.record(deviceWarnLog);
        } catch (DuplicateKeyException ignored){
            //出现重复插入，说明有重复消息，直接忽略即可
        }
    }

}
