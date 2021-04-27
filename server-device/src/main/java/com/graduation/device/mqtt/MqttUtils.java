package com.graduation.device.mqtt;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class MqttUtils {
    @Autowired
    private MqttClient client;

    private static MqttUtils mqttUtils;

    @PostConstruct
    public void init() {
        mqttUtils = this;
    }

    //发布单个主题，默认qos为2
    public static void publish(String topic, Object msg) {
        publish(topic, msg, 2, false);
    }

    //发布单个主题
    public static void publish(String topic, Object msg, int qos, boolean retained) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        String msgStr = JSONObject.toJSONString(msg);
        message.setPayload(msgStr.getBytes());
        MqttTopic mTopic = mqttUtils.client.getTopic(topic);
        if (mTopic == null) {
            log.error("topic not exist");
            return ;
        }
        MqttDeliveryToken token;
        try {
            log.info("topic: " + topic + " msg: " + msgStr);
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //订阅某个主题，qos默认为2
    public static void subscribe(String topic) {
        subscribe(topic, 2);
    }

    //订阅某个主题
    public static void subscribe(String topic, int qos) {
        try {
            mqttUtils.client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //取消订阅某个主题
    public static void unsubscribe(String topic) {
        try {
            mqttUtils.client.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
