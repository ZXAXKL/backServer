package com.graduation.device.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Configuration
public class MqttConfig {
    //mqtt主机位置
    @Value("${spring.mqtt.host:tcp://localhost}")
    private String host;

    //客户端id
    @Value("${spring.mqtt.client-id}")
    private String clientId;

    //用户名
    @Value("${spring.mqtt.username:}")
    private String username;

    //密码
    @Value("${spring.mqtt.password:}")
    private String password;

    //超时
    @Value("${spring.mqtt.timeout:30}")
    private Integer timeout;

    //心跳
    @Value("${spring.mqtt.keepalive:60}")
    private Integer keepalive;

    @Value("${spring.mqtt.reconnect:true}")
    private boolean reconnect = true;

    @Value("${spring.mqtt.interval:5}")
    private Integer interval;

    //注入所有实现类
    /*@Autowired(required = false)
    private List<MqttReceiver> mqttReceivers;

    private Map<String, MqttReceiver> mqttReceiverMap = new HashMap<>();

    @Autowired
    void initReceiverMap(){
        for (MqttReceiver mqttReceiver : mqttReceivers) {
            mqttReceiverMap.put(mqttReceiver.topic(), mqttReceiver);
        }
    }*/

    @Autowired(required = false)
    private Map<String, MqttReceiver> mqttReceiverMap;

    private List<MqttReceiver> mqttReceivers = new ArrayList<>();

    @Autowired
    void initReceiverMap(){
        mqttReceiverMap.forEach((key, value) -> {
            mqttReceivers.add(value);
        });
    }

    //构建完整的主题
    private String buildTopic(MqttReceiver mqttReceiver){
        //这里之所以有两个斜杠的原因是因为$share/group/不参与主题匹配的计算，而我们的主题最开始有一个斜杠
        return "$share/" + mqttReceiver.group() + "//device/" + mqttReceiver.topic();
    }

    //根据配置构造连接参数
    @Bean
    public MqttConnectOptions mqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setConnectionTimeout(timeout);
        mqttConnectOptions.setKeepAliveInterval(keepalive);
        return mqttConnectOptions;
    }

    //注入连接参数，创建客户端并发起连接
    @Bean
    public MqttClient mqttClient(MqttConnectOptions mqttConnectOptions) throws MqttException {
        //生成客户端对象
        MqttClient mqttClient = new MqttClient(
                host,
                clientId == null ? UUID.randomUUID().toString() : clientId + UUID.randomUUID().toString().substring(0, 8),
                new MemoryPersistence()
        );
        //构造公共回调对象
        mqttClient.setCallback(new MqttCallback() {
            //连接断开做重连
            @Override
            public void connectionLost(Throwable cause) {
                cause.printStackTrace();
                log.error("Disconnect and Attempt to reconnect to MQTT server...");
                if(reconnect){
                    connect(mqttClient, mqttConnectOptions);
                }
            }

            //发送结果
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                if (token.isComplete()) {
                    log.info("Successful publish");
                } else {
                    log.error("Failure publish");
                }
            }

            //监听到信息的回调
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                try {
                    //调用主题对应的消费方法
                    log.info("topic: " + topic);
                    log.info("Message received: " + message);
                    MqttReceiver mqttReceiver = mqttReceiverMap.get(topic.split("/")[2]);
                    if(mqttReceiver != null){
                        mqttReceiver.consume(topic, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //发起连接
        if(reconnect){
            connect(mqttClient, mqttConnectOptions);
        }else {
            simpleConnect(mqttClient, mqttConnectOptions);
        }

        return mqttClient;
    }

    //异步连接服务器
    private void connect(MqttClient mqttClient, MqttConnectOptions mqttConnectOptions) {
        new Thread() {
            public void run() {
                int i = 0;
                //死循环，直到连接上为止
                while(true){
                    try {
                        if (mqttClient.isConnected()) {
                            mqttClient.disconnect();
                        }
                        mqttClient.connect(mqttConnectOptions);

                        //订阅默认的主题
                        for (MqttReceiver mqttReceiver : mqttReceivers) {
                            mqttClient.subscribe(buildTopic(mqttReceiver), mqttReceiver.qos());
                            //下面的方法因为版本不支持共享订阅所以暂时注释
                            //mqttReceiver.subscribe(mqttClient);
                        }

                        log.info("Successfully connected to MQTT server");
                        break;
                    } catch (Exception e1) {
                        try{
                            e1.printStackTrace();
                            Thread.sleep(interval * 1000);
                            ++i;
                            log.error("Attempt to connect to MQTT server failed: " + i + " times");
                        }catch (Exception e2){
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    private void simpleConnect(MqttClient mqttClient, MqttConnectOptions mqttConnectOptions){
        try {
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
            mqttClient.connect(mqttConnectOptions);

            //订阅默认的主题
            for (MqttReceiver mqttReceiver : mqttReceivers) {
                mqttClient.subscribe(buildTopic(mqttReceiver), mqttReceiver.qos());
                //下面的方法因为版本不支持共享订阅所以暂时注释
                //mqttReceiver.subscribe(mqttClient);
            }
            log.info("Successfully connected to MQTT server");;
        } catch (Exception e1) {
            try{
                e1.printStackTrace();
                log.error("Attempt to connect to MQTT server failed");
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }
}