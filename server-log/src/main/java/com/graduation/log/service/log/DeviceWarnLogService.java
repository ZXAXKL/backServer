package com.graduation.log.service.log;

import com.graduation.common.date.DateUtils;
import com.graduation.log.mapper.DeviceWarnLogMapper;
import com.graduation.log.service.feign.DeviceService;
import com.graduation.log.service.feign.RoomService;
import com.graduation.log.table.DeviceWarnLog;
import com.graduation.log.table.WarnLogTransfer;
import com.graduation.log.vo.DeviceWarnLogVoToUser;
import com.graduation.user.mapper.RoomMapper;
import com.graduation.user.mapper.UserMapper;
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

    @Autowired
    private DeviceService deviceService;

    private DeviceWarnLogVoToUser deviceWarnLogVoToUser;

    //存入数据库
    public void record(DeviceWarnLog deviceWarnLog){
        deviceWarnLogMapper.insertSelective(deviceWarnLog);
    }

    //查询房间的报警记录
    public List<WarnLogTransfer> view(Integer userId, String... properties){
        List<Room> roomList = roomService.getRoom(userId);
        List<DeviceWarnLog> deviceWarnLogList = new ArrayList<>();
        for(Room room: roomList){
            Example example = new Example(DeviceWarnLog.class);
            if(properties.length > 0){
                example.selectProperties(properties);
            }
            example.createCriteria().andEqualTo("roomId", room.getId());
            example.orderBy("confirmDate").orderBy("createDate");
            deviceWarnLogList.addAll(deviceWarnLogMapper.selectByExample(example));
        }
        List<WarnLogTransfer> warnLogTransfers = new ArrayList<>();
        for(DeviceWarnLog deviceWarnLog: deviceWarnLogList){
            System.out.println(deviceWarnLog);
            WarnLogTransfer warnLogTransfer = new WarnLogTransfer();
            warnLogTransfer.setId(deviceWarnLog.getId());
            warnLogTransfer.setRoomId(deviceWarnLog.getRoomId());
            warnLogTransfer.setSn(deviceWarnLog.getSn());
            warnLogTransfer.setCreateDate(deviceWarnLog.getCreateDate());
            if(deviceWarnLog.getConfirmId() != null){
                warnLogTransfer.setConfirmId(deviceWarnLog.getConfirmId());
                warnLogTransfer.setConfirmDate(deviceWarnLog.getConfirmDate());
            }
            warnLogTransfer.setInfo(deviceWarnLog.getInfo());
            warnLogTransfers.add(warnLogTransfer);
        }
        for(WarnLogTransfer warnLogTransfer: warnLogTransfers){
            warnLogTransfer.setRoomName(roomService.getRoomName(warnLogTransfer.getRoomId()));
            warnLogTransfer.setDeviceName(deviceService.getDeviceName(warnLogTransfer.getDeviceName()));
        }
        System.out.println(warnLogTransfers);
        return warnLogTransfers;
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
