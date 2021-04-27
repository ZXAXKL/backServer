package com.graduation.device.logic.mqtt.listener;

import com.alibaba.fastjson.JSONObject;
import com.graduation.device.logic.mqtt.explain.WarnMsgExplainer;
import com.graduation.device.mapper.DeviceMapper;
import com.graduation.device.mqtt.MqttReceiver;
import com.graduation.device.service.rabbitmq.log.DeviceWarningLogProducer;
import com.graduation.device.service.rabbitmq.notify.DeviceWarningMsgProducer;
import com.graduation.device.table.Device;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("warn")
public class DeviceWarningListener extends MqttReceiver {
    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceWarningMsgProducer deviceWarningMsgProducer;

    @Autowired
    private DeviceWarningLogProducer deviceWarningLogProducer;

    @Autowired
    private Map<String, WarnMsgExplainer> explainMap;

    @PostConstruct
    public void init(){
        //建立命令对应处理类的映射
        Map<String, WarnMsgExplainer> map = new HashMap<>();
        explainMap.forEach((key, value) -> {
            map.put(value.cmd(), value);
        });
        explainMap = map;
    }

    @Override
    protected String topic() {
        return "warn/+";
    }

    @Override
    protected void consume(String topic, MqttMessage message) {

        //解析消息
        JSONObject jsonObject;
        try {
            //这里如果抛出异常，说明json串格式不对，直接return，如果解析出的为null，相当于空消息，可以直接跳过
            if((jsonObject = JSONObject.parseObject(new String(message.getPayload(), StandardCharsets.UTF_8))) == null){
                return;
            }
        } catch (Exception e){
            return;
        }

        //获取到消息中的cmd
        String cmd = jsonObject.getString("cmd");
        //获取解析类
        WarnMsgExplainer explainer = explainMap.get(cmd);
        //如果没有对应的解析类，直接返回
        if(explainer == null){
            return;
        }

        //判断是否可以忽略
        if(explainer.ignore(jsonObject)){
            return;
        }

        //从主题获取imei
        String sn = getSnFromTopic(topic);

        //查设备信息
        Device device = new Device();
        device.setSerialNumber(sn);
        device = deviceMapper.selectOne(device);
        //检查设备是否存在
        if(device == null){
            return;
        }
        //发布给日志服务处理，记录日志
        deviceWarningLogProducer.send(device.getSerialNumber(), explainer.extract(jsonObject));
        //发布给用户服务处理，通知用户
        deviceWarningMsgProducer.send(device.getSerialNumber(), explainer.explain(jsonObject));
    }
}

