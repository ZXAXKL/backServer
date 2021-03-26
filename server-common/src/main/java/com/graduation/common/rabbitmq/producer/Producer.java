package com.graduation.common.rabbitmq.producer;

import com.alibaba.fastjson.JSONObject;
import com.graduation.common.rabbitmq.RabbitMqConfig;

public abstract class Producer extends ProducerBase{
    //普通的生产者,提供公开的发送信息的接口，扩展性高
    //发送延时消息
    public final MessageHandler sendDelayMsg(String routingKey, JSONObject msg, int delay, String sn){
        return send(RabbitMqConfig.DELAY_EXCHANGE, routingKey, msg, delay, sn);
    }

    public final MessageHandler sendDelayMsg(String routingKey, JSONObject msg, int delay){
        return send(RabbitMqConfig.DELAY_EXCHANGE, routingKey, msg, delay, null);
    }

    //发送直接消息
    public final MessageHandler sendDirectMsg(String routingKey, JSONObject msg, String sn){
        return send(RabbitMqConfig.DIRECT_EXCHANGE, routingKey, msg, 0, sn);
    }

    public final MessageHandler sendDirectMsg(String routingKey, JSONObject msg){
        return send(RabbitMqConfig.DIRECT_EXCHANGE, routingKey, msg, 0, null);
    }

    //发送广播消息
    public final MessageHandler sendFanoutMsg(JSONObject msg, String sn){
        return send(RabbitMqConfig.FANOUT_EXCHANGE, "", msg, 0, sn);
    }

    public final MessageHandler sendFanoutMsg(JSONObject msg){
        return send(RabbitMqConfig.FANOUT_EXCHANGE, "", msg, 0, null);
    }

    //发送主题消息
    public final MessageHandler sendTopicMsg(String routingKey, JSONObject msg, String sn){
        return send(RabbitMqConfig.TOPIC_EXCHANGE, routingKey, msg, 0, sn);
    }

    public final MessageHandler sendTopicMsg(String routingKey, JSONObject msg){
        return send(RabbitMqConfig.TOPIC_EXCHANGE, routingKey, msg, 0, null);
    }
}
