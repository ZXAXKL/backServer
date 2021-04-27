package com.graduation.device.logic.admin;

import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.device.service.device.DeviceService;
import com.graduation.device.table.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceAdminLogic {

    @Autowired
    private DeviceService deviceService;

    //根据ID查询设备
    public ResponseDto findById(Integer id){
        Device device = deviceService.existById(id);
        if(device == null){
            return ResultUtils.warning("设备不存在");
        }
        return ResultUtils.success("查询成功", device);
    }

    //根据sn查询设备
    public ResponseDto findBySerialNumber(String sn){
        Device device = deviceService.existBySerialNumber(sn);
        if(device == null){
            return ResultUtils.warning("设备不存在");
        }
        return ResultUtils.success("查询成功", device);
    }
}
