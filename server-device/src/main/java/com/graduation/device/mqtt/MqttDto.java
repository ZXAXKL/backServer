package com.graduation.device.mqtt;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MqttDto {

    //类型
    private String type;
    //操作
    private String cmd;
    //参数 可能为map集合也可以为单个数值
    private Object args;

    //构造函数
    private MqttDto(String type, String cmd, Object args){
        this.type = type;
        this.cmd = cmd;
        this.args = args;
    }

    //构造函数，不传参数则认为args为一个集合
    private MqttDto(String type, String cmd){
        this.type = type;
        this.cmd = cmd;
        this.args = new HashMap<>();
    }

    //生成一个空参数的传输对象
    public static MqttDto makeEmptyMqttDto(String type, String cmd){
        return new MqttDto(type, cmd, "");
    }

    //生成一个单参数传输对象
    public static MqttDto makeSigArgMqttDto(String type, String cmd, Object args){
        return new MqttDto(type, cmd, args);
    }

    //生成一个多参数的传输对象
    public static MqttDto makeMulArgMqttDto(String type, String cmd){
        return new MqttDto(type, cmd);
    }

    //往参数集合里添加参数
    @SuppressWarnings("unchecked")
    public void put(String key, Object value){
        if(args instanceof Map){
            ((Map) args).put(key, value);
        }
    }

}
