package com.graduation.common.rabbitmq.consumer.delay;

import com.graduation.common.rabbitmq.consumer.Consumer;

public abstract class DelayConsumer extends Consumer {
    //消息类型
    public final String type(){
        return "delay_msg";
    }
}
