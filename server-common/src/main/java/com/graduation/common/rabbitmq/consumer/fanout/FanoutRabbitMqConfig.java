package com.graduation.common.rabbitmq.consumer.fanout;

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
public class FanoutRabbitMqConfig {

    //注入所有的队列
    @Autowired(required = false)
    private Map<String, FanoutConsumer> fanoutConsumers;

    //给所有的广播队列绑定交换机
    @Autowired
    public void bind(RabbitAdmin rabbitAdmin, ConnectionFactory connectionFactory, BeanUtils beanUtils) {
        if(this.fanoutConsumers != null){
            //创建交换机
            CustomExchange exchange = new CustomExchange("fanout_exchange", "fanout", true, false);
            //绑定队列和消费者
            RabbitMqConfig.bind(rabbitAdmin, connectionFactory, fanoutConsumers, exchange, beanUtils);
        }
    }
}

