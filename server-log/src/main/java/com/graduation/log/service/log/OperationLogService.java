package com.graduation.log.service.log;

import com.graduation.common.date.DateUtils;
import com.graduation.log.mapper.OperationLogMapper;
import com.graduation.log.table.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    //获得房间的所有操作日志
    public List<OperationLog> view(Integer roomId, String... properties){
        Example example = new Example(OperationLog.class);
        if(properties.length > 0){
            example.selectProperties(properties);
        }
        example.createCriteria().andEqualTo("room_id", roomId);
        example.orderBy("createTime").desc();
        return operationLogMapper.selectByExample(example);
    }

    //记录日志
    public void record(Integer roomId, String info, Date createTime){
        //构造日志对象
        OperationLog operationLog = new OperationLog();
        operationLog.setRoomId(roomId);
        operationLog.setInfo(info);
        //插入数据库
        operationLogMapper.insertSelective(operationLog);
    }

    //清理超过n天的记录
    public void clean(int days){
        Example example = new Example(OperationLog.class);
        example.createCriteria().andLessThan("createTime", DateUtils.calculate(new Date(), -1 * days, TimeUnit.DAYS));
    }

}