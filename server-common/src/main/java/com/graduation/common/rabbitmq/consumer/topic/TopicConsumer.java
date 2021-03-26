package com.graduation.common.rabbitmq.consumer.topic;

import com.graduation.common.rabbitmq.consumer.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TopicConsumer extends Consumer {
    //消息类型
    public final String type(){
        return "topic_msg";
    }
}
