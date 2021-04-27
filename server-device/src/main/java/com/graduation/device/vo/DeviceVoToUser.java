package com.graduation.device.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.graduation.device.table.Device;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceVoToUser {

    //该数组用于查询数据库，其包含设备信息传输对象所有的字段，外加组id（用来分组）
    public static final String[] columns = {
            "id", "serialNumber", "name", "state"
    };

    //网控器id
    private Integer id;

    //网控器序列号
    private String serialNumber;

    public DeviceVoToUser(Device device){
        this.id = device.getId();
        this.serialNumber = device.getSerialNumber();
    }

}

