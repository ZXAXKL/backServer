package com.graduation.device.logic.mqtt.explain.impl;

import com.alibaba.fastjson.JSONObject;
import com.graduation.device.logic.mqtt.explain.WarnMsgExplainer;
import org.springframework.stereotype.Component;

@Component
public class TemperatureWarnMsgExplainer implements WarnMsgExplainer {
    @Override
    public String cmd() {
        return "t_warn";
    }

    @Override
    public String explain(JSONObject errorMsg) {
        return String.format("设备%温度检测异常", errorMsg.getJSONObject("args").getString("serialnumber"));
    }

    @Override
    public JSONObject extract(JSONObject errorMsg) {
        return errorMsg.getJSONObject("args");
    }
}
