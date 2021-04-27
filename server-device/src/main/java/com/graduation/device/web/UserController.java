package com.graduation.device.web;

import com.graduation.common.result.ResponseDto;
import com.graduation.device.logic.user.shell.DeviceUserLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags={"用户接口"})
@RequestMapping("/user")
public class  UserController {

    @Autowired
    private DeviceUserLogic deviceUserLogic;


    //获取机房的所有设备信息
    @PostMapping("/device/get")
    @ApiOperation(value="机房设备查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "机房ID", dataType = "Integer", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto getGroups(@RequestParam Integer roomId){
        return deviceUserLogic.get(roomId);
    }

    //检查一台设备是否绑定
    @PostMapping("/device/check")
    @ApiOperation(value="检查设备是否绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sn", value = "设备序列号", dataType = "String", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto checkItem(@RequestParam String sn){
        return deviceUserLogic.check(sn);
    }

    //发送清空设备命令，用于绑定前
    @PostMapping("/device/bind")
    @ApiOperation(value="绑定前清空设备信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sn", value = "设备序列号", dataType = "String", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto bind(
            @RequestParam String sn
    ){
        return deviceUserLogic.bind(sn);
    }

    //确认绑定一台设备
    @PostMapping("/device/confirm/bind")
    @ApiOperation(value="设备绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "机房ID", dataType = "Integer", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "sn", value = "设备序列号", dataType = "String", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "name", value = "设备名称", dataType = "String", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto bindConfirm(
            @RequestParam Integer roomId,
            @RequestParam String sn,
            @RequestParam String name
    ){
        return deviceUserLogic.bindConfirm(roomId, sn, name);
    }

    //解绑一台设备
    @PostMapping("/device/unbind")
    @ApiOperation(value="设备解绑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "机房ID", dataType = "Integer", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "deviceId", value = "设备ID", dataType = "Integer", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto unbindItem(@RequestParam Integer roomId, @RequestParam Integer deviceId){
        return deviceUserLogic.unbind(roomId, deviceId);
    }

    //修改名字
    @PostMapping("/device/rename")
    @ApiOperation(value="设备重命名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "机房ID", dataType = "Integer", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "deviceId", value = "设备ID", dataType = "Integer", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "name", value = "设备名称", dataType = "String", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto renameItem(@RequestParam Integer roomId, @RequestParam Integer deviceId, @RequestParam String name){
        return deviceUserLogic.rename(roomId, deviceId, name);
    }

}
