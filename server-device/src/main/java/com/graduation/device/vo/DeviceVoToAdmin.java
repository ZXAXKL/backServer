package com.graduation.device.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.graduation.device.table.Device;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceVoToAdmin {

    private Integer id;

    private String serialNumber;

    public DeviceVoToAdmin(Device device){
        //获取id
        this.id = device.getId();
        //获取序列号
        this.serialNumber = device.getSerialNumber();
    }

}

