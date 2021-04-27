package com.graduation.log.logic.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.log.service.log.DeviceWarnLogService;
import com.graduation.log.table.DeviceWarnLog;
import com.graduation.log.table.WarnLogTransfer;
import com.graduation.log.vo.DeviceWarnLogVoToUser;
import com.graduation.log.vo.WarnLogVoToUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DeviceWarnLogUserLogic {

    @Autowired
    private DeviceWarnLogService deviceWarnLogService;

    //分页查询警告日志
    public ResponseDto view(Integer userId, Integer pageNum, Integer pageSize){
        //分页获取到用户所有的警告信息（除数据部分）的列表
        PageHelper.startPage(pageNum, pageSize);
        List<WarnLogTransfer> logs = deviceWarnLogService.view(userId);
        PageInfo<WarnLogTransfer> pageInfo = new PageInfo<>(logs);
        List<WarnLogVoToUser> result = new ArrayList<>();
        //把列表里的类型转传输类型
        for(WarnLogTransfer log : pageInfo.getList()){
            result.add(new WarnLogVoToUser(log));
        }
        //包装结果
        Map<String, Object> map = new HashMap<>();
        map.put("list", result);
        map.put("pages", pageInfo.getPages());
        map.put("total", pageInfo.getTotal());
        //返回结果
        return ResultUtils.success("查询成功", map);
    }

    //确认某个警告
    public ResponseDto read(Integer roomId, Integer userId, Long id){
        //检查该用户是否存在这条警告
        DeviceWarnLog log = deviceWarnLogService.exist(id);
        if(log == null || !log.getRoomId().equals(roomId)){
            return ResultUtils.error("不存在这条警告");
        }
        //确认这条警告
        deviceWarnLogService.confirm(id, userId);
        return ResultUtils.success("确认成功");
    }

}