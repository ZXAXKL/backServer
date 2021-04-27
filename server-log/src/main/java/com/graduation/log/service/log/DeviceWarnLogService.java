package com.graduation.log.service.log;

import com.graduation.common.date.DateUtils;
import com.graduation.log.mapper.DeviceWarnLogMapper;
import com.graduation.log.service.feign.RoomService;
import com.graduation.log.table.DeviceWarnLog;
import com.graduation.user.service.user.UserService;
import com.graduation.user.table.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceWarnLogService {

    @Autowired
    private DeviceWarnLogMapper deviceWarnLogMapper;

    @Autowired
    private RoomService roomService;

    //存入数据库
    public void record(DeviceWarnLog deviceWarnLog){
        deviceWarnLogMapper.insertSelective(deviceWarnLog);
    }

    //查询房间的报警记录
    public List<DeviceWarnLog> view(Integer userId, String... properties){
        List<Room> roomList = roomService.getRoom(userId);
        Example example = new Example(DeviceWarnLog.class);
        List<DeviceWarnLog> deviceWarnLogList = new ArrayList<>();
        for(Room room: roomList){
            if(properties.length > 0){
                example.selectProperties(properties);
            }
            example.createCriteria().andEqualTo("roomId", room.getId());
            example.orderBy("confirm_date").orderBy("create_date");
            deviceWarnLogList.add(deviceWarnLogMapper.selectByPrimaryKey(example));
        }
        return deviceWarnLogList;
    }

    //获取某条记录
    public DeviceWarnLog exist(Long id, String... properties){
        Example example = new Example(DeviceWarnLog.class);
        if(properties.length > 0){
            example.selectProperties(properties);
        }
        example.createCriteria().andEqualTo("id", id);
        return deviceWarnLogMapper.selectOneByExample(example);
    }

    //确认某条警告
    public void confirm(Long id, Integer userId){
        DeviceWarnLog deviceWarnLog = new DeviceWarnLog();
        deviceWarnLog.setId(id);
        deviceWarnLog.setConfirmId(userId);
        deviceWarnLog.setConfirmDate(DateUtils.getTimestamp());
        deviceWarnLogMapper.updateByPrimaryKeySelective(deviceWarnLog);
    }

}
