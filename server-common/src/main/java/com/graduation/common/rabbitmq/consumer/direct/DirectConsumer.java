package com.graduation.common.rabbitmq.consumer.direct;

import com.graduation.common.rabbitmq.consumer.Consumer;

public abstract class DirectConsumer extends Consumer {
    //消息类型
    public final String type(){
        return "direct_msg";
    }
}
