package com.graduation.common.rabbitmq.consumer.direct;

import com.graduation.common.bean.BeanUtils;
import com.graduation.common.rabbitmq.RabbitMqConfig;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Configuration
public class DirectRabbitMqConfig {
    //注入所有的队列
    @Autowired(required = false)
    private Map<String, DirectConsumer> directConsumers;

    //绑定交换机、队列和消费方法
    @Autowired
    public void bind(RabbitAdmin rabbitAdmin, ConnectionFactory connectionFactory, BeanUtils beanUtils) {
        if(this.directConsumers != null){
            //创建交换机
            CustomExchange exchange = new CustomExchange("direct_exchange", "direct", true, false);
            //绑定队列和消费者
            RabbitMqConfig.bind(rabbitAdmin, connectionFactory, directConsumers, exchange, beanUtils);
        }
    }
}

