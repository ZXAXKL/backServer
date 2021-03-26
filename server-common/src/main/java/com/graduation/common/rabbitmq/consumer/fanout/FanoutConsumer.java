package com.graduation.common.rabbitmq.consumer.fanout;

import com.graduation.common.rabbitmq.consumer.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class FanoutConsumer extends Consumer {
    //消息类型
    public final String type(){
        return "fanout_msg";
    }
}
