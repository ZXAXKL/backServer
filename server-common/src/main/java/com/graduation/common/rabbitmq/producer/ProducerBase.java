package com.graduation.common.rabbitmq.producer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


abstract class ProducerBase{

    //专属的消息发送模板类
    RabbitTemplate rabbitTemplate;

    //生成一个专属的rabbitTemplate
    @Autowired
    void createRabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        this.rabbitTemplate = rabbitTemplate;
    }

    //发送消息
    MessageHandler send(String exchange, String routingKey, JSONObject msg, int delay, String sn){
        MessageHandler myMessage = new MessageHandler(
                exchange,
                routingKey,
                msg,
                delay,
                sn
        );
        myMessage.send();

        return myMessage;
    }

    //内部类，用于发送消息的类，功能： 重发消息、统计发送某个消息的次数
    protected class MessageHandler {
        private String exchange;
        private String routingKey;
        private JSONObject msg;
        private int delay;
        private String sn;
        private int count;

        MessageHandler(String exchange, String routingKey, JSONObject msg, int delay, String sn){
            this.exchange = exchange;
            this.routingKey = routingKey;
            this.msg = msg;
            this.delay = delay;
            this.sn = sn;
            this.count = 0;
        }

        //投递消息
        public void send(){
            rabbitTemplate.convertAndSend(
                    exchange,
                    routingKey,
                    msg,
                    message -> {
                        //配置消息的过期时间
                        message.getMessageProperties().setDelay(delay);
                        message.getMessageProperties().setTimestamp(new Date());
                        message.getMessageProperties().setCorrelationId(sn);
                        return message;
                    }
            );
            ++ this.count;
        }

        //统计次数
        public int getCount(){
            return count;
        }
    }
}
