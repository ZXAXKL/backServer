package com.graduation.common.rabbitmq.producer;

import com.alibaba.fastjson.JSONObject;
import com.graduation.common.rabbitmq.RabbitMqConfig;
import com.graduation.common.redis.sn.SerialNumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class ReliableProducer extends ProducerBase implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback {

    //可靠的生产者,提供一套默认的重试机制，保证消息投递的可靠性
    //缓存发出去的信息，便于重发
    private Map<String, MessageHandler> messageHandlers = new HashMap<>();

    @Autowired
    protected SerialNumberUtils serialNumberUtils;

    @Override
    @Autowired
    protected final void createRabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //绑定两个回调方法
        rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setMessageConverter(messageConverter);
        this.rabbitTemplate = rabbitTemplate;
    }

    //重试次数上限
    protected abstract Integer limit();

    //发送间隔（s）
    protected abstract Integer interval();

    //重发消息
    private void resend(String sn){
        //异步
        new Thread(() -> {
            try {
                Thread.sleep(interval() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //根据sn从缓存里获取消息发送对象
            MessageHandler messageHandler = messageHandlers.get(sn);
            //判断一下，是不是发送超过了上限次数
            if(messageHandler.getCount() < limit()){
                //重新发送
                log.info("Attempt to resend msg(" + sn + ") " + messageHandler.getCount() + " times");
                messageHandlers.get(sn).send();
            }else {
                //达到上限，停止发送
                log.info("Too many retries, discard this msg(" + sn + ")");
            }
        }).start();
    }

    //从交换机到队列失败回调
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        //获取sn
        String sn = message.getMessageProperties().getCorrelationId();
        log.error("Send msg(" + sn + ") from exchange to queue failed");
        resend(sn);
    }

    //发送到交换机回调
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //判断是否失败
        if(!ack){
            //获取sn
            String sn = correlationData.getId();
            log.error("Send msg(" + sn + ") to exchange failed");
            resend(sn);
        }
    }

    //重写基类的发送方法，把发送对象缓存在map里
    @Override
    MessageHandler send(String exchange, String routingKey, JSONObject msg, int delay, String sn) {
        return messageHandlers.put(sn, super.send(exchange, routingKey, msg, delay, sn));
    }

    //发送延时消息
    public final MessageHandler sendDelayMsg(String routingKey, JSONObject msg, int delay, String sn){
        return send(RabbitMqConfig.DELAY_EXCHANGE, routingKey, msg, delay, sn);
    }

    //发送直接消息
    public final MessageHandler sendDirectMsg(String routingKey, JSONObject msg, String sn){
        return send(RabbitMqConfig.DIRECT_EXCHANGE, routingKey, msg, 0, sn);
    }

    //发送广播消息
    public final MessageHandler sendFanoutMsg(JSONObject msg, String sn){
        return send(RabbitMqConfig.FANOUT_EXCHANGE, "", msg, 0, sn);
    }

    // 发送主题消息
    public final MessageHandler sendTopicMsg(String routingKey,JSONObject msg, String sn){
        return send(RabbitMqConfig.TOPIC_EXCHANGE, routingKey, msg, 0, sn);
    }
}
