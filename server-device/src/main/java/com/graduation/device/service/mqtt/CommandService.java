package com.graduation.device.service.mqtt;

import com.graduation.device.mqtt.MqttDto;
import com.graduation.device.mqtt.MqttUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommandService {

    //主题前缀
    private static final String PREFIX_ACTION = "/server/action/";
    private static final String PREFIX_VALID = "/server/valid/";
    private static final String PREFIX_WARN = "/device/warn/";

    //配置相关
    //控制器级
    private static final String ACTION_BIND_DEVICE = "b_device";
    private static final String ACTION_UNBIND_DEVICE = "u_device";
    private static final String ACTION_SET_DEVICE = "s_device";
    //通道级
    private static final String ACTION_SET_PORT = "s_port";
    private static final String ACTION_UNSET_PORT = "u_port";
    private static final String ACTION_TIMER_PORT = "s_timer";

    //查询相关
    private static final String ACTION_READ_CURRENT = "current";
    private static final String ACTION_READ_CSQ = "csq";
    private static final String ACTION_READ_STATUS = "status";

    //控制网控器过期的命令
    private static final String VALID_EXPIRE = "s_valid";

    private static final String TYPE_CONFIG = "config";
    private static final String TYPE_CONTROL = "control";
    private static final String TYPE_READ = "read";
    private static final String TYPE_REPORT = "report";

    //抽出公共逻辑,发送mqtt消息
    private void command(String sn, MqttDto mqttDto, Boolean retained, String prefix){
        MqttUtils.publish(prefix + sn, mqttDto, 2, retained);
    }

    //命令类型消息
    private void action(String sn, MqttDto mqttDto, Boolean retained){
        command(sn, mqttDto, retained, PREFIX_ACTION);
    }

    private void action(String sn, MqttDto mqttDto){
        action(sn, mqttDto, false);
    }

    //权限型消息
    private void valid(String sn, MqttDto mqttDto, Boolean retained){
        command(sn, mqttDto, retained, PREFIX_VALID);
    }

    private void valid(String sn, MqttDto mqttDto) {
        valid(sn, mqttDto, true);
    }

    //警告主题，清空离线保留消息
    public void offLineWarnClean(Long imei){
        MqttUtils.publish(PREFIX_WARN + imei, MqttDto.makeSigArgMqttDto(TYPE_REPORT, "offline", 1), 2, true);
    }

    //用于绑定设备前
    public void bind(String sn){
        MqttDto mqttDto = MqttDto.makeMulArgMqttDto(TYPE_CONFIG, ACTION_BIND_DEVICE);
        action(sn, mqttDto);
    }

    //解绑设备
    public void unbind(String sn){
        action(sn, MqttDto.makeEmptyMqttDto(TYPE_CONFIG, ACTION_UNBIND_DEVICE), true);
    }

}
