package com.graduation.common.rabbitmq.consumer.delay;

import com.graduation.common.bean.BeanUtils;
import com.graduation.common.rabbitmq.RabbitMqConfig;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Configuration
public class DelayRabbitMqConfig {

    //注入所有的队列
    @Autowired(required = false)
    private Map<String, DelayConsumer> delayConsumers;

    //给所有的延时队列绑定交换机
    @Autowired
    public void bind(RabbitAdmin rabbitAdmin, ConnectionFactory connectionFactory, BeanUtils beanUtils) {
        if(this.delayConsumers != null){
            Map<String, Object> args = new HashMap<>();
            args.put("x-delayed-type", "direct");
            //创建交换机
            CustomExchange exchange = new CustomExchange("delay_exchange", "x-delayed-message", true, false, args);
            //绑定队列和消费者
            RabbitMqConfig.bind(rabbitAdmin, connectionFactory, delayConsumers, exchange, beanUtils);
        }
    }
}
