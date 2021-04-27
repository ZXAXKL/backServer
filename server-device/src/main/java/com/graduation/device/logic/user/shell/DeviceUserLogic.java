package com.graduation.device.logic.user.shell;

import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.device.logic.user.core.DeviceUserLogicCore;
import com.graduation.device.service.device.DeviceService;
import com.graduation.device.table.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceUserLogic {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceUserLogicCore deviceUserLogicCore;

    //获取机房的网控器列表
    public ResponseDto get(Integer roomId){
        //直接调用核心逻辑
        return deviceUserLogicCore.get(roomId);
    }

    //检查是否绑定
    public ResponseDto check(String sn){
        //直接调用核心逻辑
        return deviceUserLogicCore.check(sn);
    }

    //发送清空配置命令,用于绑定前
    public ResponseDto bind(String sn){
        //检查设备是否存在
        Device device = deviceService.existBySerialNumber(sn);
        if(device == null){
            return ResultUtils.error("该设备不存在");
        }
        //检查是不是被绑定了
        if(device.getRoom_id() != null){
            return ResultUtils.warning("该设备已经被绑定");
        }
        //调核心逻辑
        return deviceUserLogicCore.bind(device);
    }

    //设备绑定确认
    public ResponseDto bindConfirm(Integer roomId, String sn, String name){
        //检查设备是否存在
        Device device = deviceService.existBySerialNumber(sn);
        if(device == null){
            return ResultUtils.error("该设备不存在");
        }
        //检查是不是被绑定了
        if(device.getRoom_id() != null){
            return ResultUtils.warning("该设备已经被绑定");
        }
        //包装数据
        //设置绑定者id
        device.setRoom_id(roomId);
        //调核心逻辑
        return deviceUserLogicCore.bindConfirm(device, name);
    }

    //设备解绑
    public ResponseDto unbind(Integer roomId, Integer deviceId){
        //检查设备是否存在
        Device device = deviceService.existById(deviceId);
        if(device == null || !device.getRoom_id().equals(roomId)){
            return ResultUtils.error("你不能操作该设备");
        }
        //调核心逻辑
        return deviceUserLogicCore.unbind(device);
    }

    //修改设备别名
    public ResponseDto rename(Integer roomId, Integer deviceId, String name){
        //检查设备是否存在
        Device device = deviceService.existById(deviceId);
        if(device == null || !device.getRoom_id().equals(roomId)){
            return ResultUtils.error("你不能操作该设备");
        }
        //调核心逻辑
        return deviceUserLogicCore.rename(device, name);
    }

}

