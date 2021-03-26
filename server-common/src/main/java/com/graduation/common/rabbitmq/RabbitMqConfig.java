package com.graduation.common.rabbitmq;

import com.graduation.common.bean.BeanUtils;
import com.graduation.common.rabbitmq.consumer.Consumer;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Configuration
public class RabbitMqConfig {

    public static final String DELAY_EXCHANGE = "delay_exchange";
    public static final String DIRECT_EXCHANGE = "direct_exchange";
    public static final String FANOUT_EXCHANGE = "fanout_exchange";
    public static final String TOPIC_EXCHANGE = "topic_exchange";

    //注册rabbitAdmin 方便管理
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    public static <T extends Consumer> void bind(
            RabbitAdmin rabbitAdmin,
            ConnectionFactory connectionFactory,
            Map<String, T> consumers,
            CustomExchange exchange,
            BeanUtils beanUtils
    ){
        //声明交换机
        rabbitAdmin.declareExchange(exchange);
        //遍历消费者
        consumers.forEach((key, value) -> {
            //获取队列
            Queue queue = value.getQueue();
            //声明队列
            rabbitAdmin.declareQueue(queue);
            //绑定队列到交换机
            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(value.key()).noargs());
            //获取监听容器
            SimpleMessageListenerContainer simpleMessageListenerContainer = value.getListenerContainer(connectionFactory);
            //注入成bean
            beanUtils.inject(queue.getName() + "Listener", simpleMessageListenerContainer);
        });
    }
}
