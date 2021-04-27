package com.graduation.device.logic.mqtt.explain;

import com.alibaba.fastjson.JSONObject;

public interface WarnMsgExplainer {

    //返回这个类要解析的警告消息类型
    public String cmd();

    //把这个消息解析成一个错误信息字符串
    public String explain(JSONObject errorMsg);

    //提取args部分的内容
    public JSONObject extract(JSONObject errorMsg);

    //可以用于忽略某些执行
    default public Boolean ignore(JSONObject errorMsg){
        return false;
    }

}

