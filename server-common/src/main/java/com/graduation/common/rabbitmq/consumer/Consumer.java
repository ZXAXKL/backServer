package com.graduation.common.rabbitmq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.graduation.common.format.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

@Slf4j
public abstract class Consumer implements EnvironmentAware {

    //用于获取配置文件的属性
    public Environment environment;

    //用于environment对象
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    //生成队列实例
    public final Queue getQueue(){
        return new Queue(fullName(), durable(), exclusive(), autoDelete());
    }

    public final String fullName(){
        return FormatUtils.glue(type(), service(), name());
    }

    //消息持久化到硬盘上，防止丢失
    public Boolean durable(){
        return true;
    }

    //是否排外,默认false，一个队列可以绑定多个消费者
    public Boolean exclusive(){
        return false;
    }

    //没有消费者时是否自动删除队列,默认false，可以保证消费者重启以后收到宕机期间的消息
    public Boolean autoDelete(){
        return false;
    }

    //定义队列要绑定的交换机的类型,用于区分同一服务不同类型的队列，防止重名
    public abstract String type();

    //从配置文件获取服务名,用于区别不同服务的队列，防止重名
    public final String service(){
        return environment == null ? "default" : environment.getProperty("spring.application.name");
    }

    //定义队列名字,默认为类名
    public String name(){
        return this.getClass().getSimpleName();
    }

    //定义路由键
    public abstract String key();

    //消费方法，子类实现
    public abstract void consume(JSONObject payload, MessageProperties properties);

    //消费完成处理机制,默认发回ack确认消息，子类可重写
    public void finish(Message message, Channel channel){
        try{
            //成功消费后发回ACK
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            log.info("消息已经确认");
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    //异常处理机制,默认拒绝消息，子类可重写
    public void remedy(Message message, Channel channel){
        try{
            //channel.basicNack(system.getMessageProperties().getDeliveryTag(),false,false);
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
            log.error("发生异常，已拒绝消息");
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    //构建MessageListenerContainer，动态监听，子类不能重写
    public final SimpleMessageListenerContainer getListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //设定监听的队列
        container.setQueueNames(fullName());
        //ACK模式设置为手动ACK
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            try{
                //调用消费方法，消费消息
                consume(JSONObject.parseObject(new String(message.getBody())), message.getMessageProperties());
            }catch (Exception exception){
                //发生异常，调用异常处理方法
                remedy(message, channel);
                exception.printStackTrace();
                return;
            }
            //消费完成，收尾
            finish(message, channel);
        });
        return container;
    }
}
