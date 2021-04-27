package com.graduation.log.logic.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.log.service.log.OperationLogService;
import com.graduation.log.table.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OperationLogUserLogic {

    @Autowired
    private OperationLogService operationLogService;

    //查看操作日志
    public ResponseDto view(Integer roomId, Integer pageNum, Integer pageSize) {
        //开启分页
        PageHelper.startPage(pageNum, pageSize);
        List<OperationLog> list = operationLogService.view(roomId);
        PageInfo<OperationLog> pageInfo = new PageInfo<>(list);
        //打包数据
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageInfo.getTotal());
        map.put("pages", pageInfo.getPages());
        map.put("list", pageInfo.getList());
        return ResultUtils.success("查询成功", map);
    }
    
}