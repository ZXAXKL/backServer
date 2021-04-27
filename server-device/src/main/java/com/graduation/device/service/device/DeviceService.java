package com.graduation.device.service.device;

import com.graduation.common.sql.ConditionsConstructor;
import com.graduation.device.mapper.DeviceMapper;
import com.graduation.device.table.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class DeviceService extends ConditionsConstructor {

    @Autowired
    private DeviceMapper deviceMapper;

    //根据序列号查询网控器
    public Device existBySerialNumber(String sn, String... properties){
        Example example = new Example(Device.class);
        if(properties.length > 0){
            example.selectProperties(properties);
        }
        example.createCriteria().andEqualTo("serialNumber", sn);
        return deviceMapper.selectOneByExample(example);
    }

    //根据设备id查询网控器
    public Device existById(Integer deviceId, String... properties){
        Example example = new Example(Device.class);
        if(properties.length > 0){
            example.selectProperties(properties);
        }
        example.createCriteria().andEqualTo("id", deviceId);
        return deviceMapper.selectOneByExample(example);
    }

    //更新设备信息
    public void update(Device device){
        deviceMapper.updateByPrimaryKey(device);
    }

    //查询设备
    public List<Device> findDevice(Integer roomId, String... properties){
        Example example = new Example(Device.class);
        //过滤出指定字段
        if(properties.length > 0){
            example.selectProperties(properties);
        }
        //根据机房锁定设备
        example.createCriteria().andEqualTo("room_id", roomId);

        return deviceMapper.selectByExample(example);
    }

}