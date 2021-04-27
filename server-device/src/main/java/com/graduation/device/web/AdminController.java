package com.graduation.device.web;

import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.device.logic.admin.DeviceAdminLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@Api(tags={"管理员接口"})
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DeviceAdminLogic deviceAdminLogic;

    //查询设备信息
    @PostMapping("/item/view")
    @ApiOperation(value="设备信息查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serialNumber", value = "设备序号", dataType = "string", paramType = "query", required = false, defaultValue = ""),
            @ApiImplicitParam(name = "id", value = "设备Id", dataType = "Integer", paramType = "query", required = false, defaultValue = "")
    })
    public ResponseDto viewDevices(
            @RequestParam(required = false) String serialNumber,
            @RequestParam(required = false) Integer id
    ){
        if(serialNumber == null && id == null){
            return ResultUtils.error("至少输入一个信息");
        }else if(serialNumber == null){
            return deviceAdminLogic.findById(id);
        }else{
            return deviceAdminLogic.findBySerialNumber(serialNumber);
        }
    }
}
