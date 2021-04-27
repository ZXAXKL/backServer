package com.graduation.log.web;

import com.graduation.common.result.ResponseDto;
import com.graduation.log.logic.user.DeviceWarnLogUserLogic;
import com.graduation.log.logic.user.OperationLogUserLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@Api(tags={"用户接口"})
@RequestMapping("/user")
public class UserController {

    @Autowired
    private DeviceWarnLogUserLogic deviceWarnLogUserLogic;

    @Autowired
    private OperationLogUserLogic operationLogUserLogic;

    //分页查询告警日志
    @PostMapping("/warn/view")
    @ApiOperation(value="警告日志查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Integer", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "pageNum", value = "分页数目", dataType = "Integer", paramType = "query", required = true, defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "Integer", paramType = "query", required = true, defaultValue = "10")
    })
    public ResponseDto viewWarnLogs(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        return deviceWarnLogUserLogic.view(userId, pageNum, pageSize);
    }

    //确认某条警告消息
    @PostMapping("/warn/read")
    @ApiOperation(value="警告消息确认")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "机房ID", dataType = "Integer", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "userId", value = "员工ID", dataType = "Integer", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "id", value = "警告信息ID", dataType = "Long", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto readWarnLog(@RequestParam Integer roomId, @RequestParam Integer userId, @RequestParam Long id) throws UnsupportedEncodingException {
        return deviceWarnLogUserLogic.read(roomId, userId, id);
    }

    //分页查询操作日志
    @PostMapping("/operation/view")
    @ApiOperation(value="操作日志查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "机房ID", dataType = "Integer", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "pageNum", value = "分页数目", dataType = "Integer", paramType = "query", required = true, defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "Integer", paramType = "query", required = true, defaultValue = "10")
    })
    public ResponseDto viewOperationLogs(
            @RequestParam Integer roomId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return operationLogUserLogic.view(roomId, pageNum, pageSize);
    }

}