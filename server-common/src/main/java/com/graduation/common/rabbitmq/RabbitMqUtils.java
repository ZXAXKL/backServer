package com.graduation.common.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMqUtils {
    private static RabbitMqUtils rabbitMqUtils;

    private RabbitTemplate rabbitTemplate;

    //用于发送简单消息
    @Autowired
    public void createRabbitTemplate(ConnectionFactory connectionFactory){
        rabbitMqUtils = this;
        rabbitMqUtils.rabbitTemplate = new RabbitTemplate(connectionFactory);
    }

    //延时模式消息
    public static void delayMsg(String routingKey, String msg, int delay){
        delayMsg(routingKey, msg, delay, null);
    }

    public static void delayMsg(String routingKey, String msg, int delay, CorrelationData correlationData){
        rabbitMqUtils.rabbitTemplate.convertAndSend(
                RabbitMqConfig.DELAY_EXCHANGE,
                routingKey,
                msg,
                message -> {
                    //配置消息的过期时间
                    message.getMessageProperties().setDelay(delay);
                    return message;
                },
                correlationData
        );
    }

    //主题模式消息
    public static void topicMsg(String topic, String msg){
        topicMsg(topic, msg, null);
    }

    public static void topicMsg(String topic, String msg, CorrelationData correlationData){
        rabbitMqUtils.rabbitTemplate.convertAndSend(
                RabbitMqConfig.TOPIC_EXCHANGE,
                topic,
                msg,
                correlationData
        );
    }

    //直连模式消息
    public static void directMsg(String key, String msg){
        directMsg(key, msg, null);
    }

    public static void directMsg(String key, String msg, CorrelationData correlationData){
        rabbitMqUtils.rabbitTemplate.convertAndSend(
                RabbitMqConfig.DIRECT_EXCHANGE,
                key,
                msg,
                correlationData
        );
    }

    //广播模式消息
    public static void fanoutMsg(String msg){
        fanoutMsg(msg, null);
    }

    public static void fanoutMsg(String msg, CorrelationData correlationData){
        rabbitMqUtils.rabbitTemplate.convertAndSend(
                RabbitMqConfig.FANOUT_EXCHANGE,
                "",
                msg,
                correlationData
        );
    }
}
