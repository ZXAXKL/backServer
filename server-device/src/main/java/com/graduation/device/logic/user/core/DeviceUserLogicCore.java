package com.graduation.device.logic.user.core;

import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.device.log.Operation;
import com.graduation.device.mapper.DeviceMapper;
import com.graduation.device.service.device.DeviceService;
import com.graduation.device.service.mqtt.CommandService;
import com.graduation.device.table.Device;
import com.graduation.device.vo.DeviceVoToUser;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DeviceUserLogicCore {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private CommandService commandService;

    //获取机房的设备列表
    public ResponseDto get(Integer roomId){
        //直接取出设备列表即可
        List<Device>  deviceList = deviceService.findDevice(roomId, DeviceVoToUser.columns);
        if(deviceList.isEmpty()){
            return ResultUtils.error("机房暂无设备");
        }
        return ResultUtils.success("获取成功", deviceService.findDevice(roomId, DeviceVoToUser.columns));
    }

    //检查是否绑定
    public ResponseDto check(String sn){
        //先查询对应网控器信息
        Device device = deviceService.existBySerialNumber(sn);
        //检查是否存在
        if(device == null){
            return ResultUtils.error("该设备不存在");
        }
        //检查设备是否被绑定
        if(device.getRoom_id() == null){
            //如果没被绑定,返回sn
            return ResultUtils.success("可以绑定", device.getSerialNumber());
        }else {
            //否则被绑定
            return ResultUtils.warning("该设备已经被绑定");
        }
    }

    //发送清空配置命令,用于绑定前
    public ResponseDto bind(Device device){
        commandService.bind(device.getSerialNumber());
        return ResultUtils.success("绑定命令发送成功");
    }

    // 设备绑定确认
    @Operation(type = "device.bind")
    public ResponseDto bindConfirm(Device device, String name){
        //设置默认别名为序列号
        device.setName(name);
        //更新数据库
        deviceService.update(device);
        //返回包装后的传输层对象
        return ResultUtils.success("绑定成功", new DeviceVoToUser(device));
    }

    //设备解绑
    @Transactional
    @Operation(type = "device.unbind")
    public ResponseDto unbind(Device device){
        //清空用户id、组id和别名等用户部分数据
        device.setRoom_id(null);
        device.setName("");
        //改数据库
        deviceService.update(device);
        //发送解绑命令
        commandService.unbind(device.getSerialNumber());
        return ResultUtils.success("解绑成功");
    }

    //修改设备别名
    @Operation(type = "device.rename")
    public ResponseDto rename(Device device, String name){
        device.setName(name);
        deviceService.update(device);
        return ResultUtils.success("修改设备别名成功");
    }

}