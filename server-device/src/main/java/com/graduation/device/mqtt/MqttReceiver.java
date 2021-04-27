package com.graduation.device.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public abstract class MqttReceiver {
    //定义监听的主题，子类实现
    protected abstract String topic();

    //定义共享组-可选
    protected String group(){
        return "default";
    }

    //定义qos，默认为2
    protected int qos(){
        return 2;
    }

    //消费方法，子类实现
    protected abstract void consume(String topic, MqttMessage message);

    //订阅方法，配置类里会调用，用来在连接时自动订阅
    public final void subscribe(MqttClient mqttClient) throws MqttException {
        mqttClient.subscribe("$share/" + group() + "/" + topic(), qos(), this::consume);
    }

    //取消订阅
    public final void unsubscribe(MqttClient mqttClient) throws MqttException {
        mqttClient.unsubscribe(topic());
    }

    protected final String getSnFromTopic(String topic){
        String[] strs = topic.split("/");
        return strs[1];
    }

}
