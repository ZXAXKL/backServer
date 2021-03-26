package com.graduation.common.rabbitmq.consumer.topic;

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
public class TopicRabbitMqConfig {

    //注入所有的队列
    @Autowired(required = false)
    private Map<String, TopicConsumer> topicConsumers;

    //给所有的主题队列绑定交换机
    @Autowired
    public void bind(RabbitAdmin rabbitAdmin, ConnectionFactory connectionFactory, BeanUtils beanUtils) {
        if(this.topicConsumers != null){
            //创建交换机
            CustomExchange exchange = new CustomExchange("topic_exchange", "topic", true, false);
            //绑定队列和消费者
            RabbitMqConfig.bind(rabbitAdmin, connectionFactory, topicConsumers, exchange, beanUtils);
        }
    }
}
