package com.graduation.user.web;

import com.alibaba.fastjson.JSONObject;
import com.graduation.user.logic.user.UserRoomLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.graduation.common.result.ResponseDto;
import com.graduation.user.logic.auth.user.login.impl.WeChatLogin;
import com.graduation.user.table.UserNotify;
import com.graduation.user.logic.system.MobileMessageLogic;
import com.graduation.user.logic.user.NotifyUserLogic;
import com.graduation.user.logic.user.OperateUserLogic;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = {"用户接口"})
@RequestMapping("/user")
public class UserController {

    @Autowired
    private WeChatLogin weChatLogin;

    @Autowired
    private NotifyUserLogic notifyUserLogic;

    @Autowired
    private MobileMessageLogic mobileMessageLogic;

    @Autowired
    private OperateUserLogic operateUserLogic;

    @Autowired
    private UserRoomLogic userRoomLogic;

    //微信小程序登陆 or 注册
    @ApiOperation(value = "用户登录微信小程序或注册")
    @PostMapping("/mini/wx/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "用户code", dataType = "string", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "rawData", value = "rawdata数据", dataType = "string", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "signature", value = "签名", dataType = "string", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "encryptedData", value = "加密数据encryptedData", dataType = "string", paramType = "query", required = false, defaultValue = ""),
            @ApiImplicitParam(name = "iv", value = "偏移量iv", dataType = "string", paramType = "query", required = false, defaultValue = "")
    })
    public ResponseDto login(@RequestParam String code,
                             @RequestParam String rawData,
                             @RequestParam String signature,
                             @RequestParam(required = false) String encryptedData,
                             @RequestParam(required = false) String iv
    ){
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("rawData", rawData);
        map.put("signature", signature);
        map.put("encryptedData", encryptedData);
        map.put("iv", iv);

        System.out.println(JSONObject.toJSONString(map));

        return weChatLogin.login(map);
    }

    //发验证码
    @ApiOperation(value = "用户接收验证码")
    @RequestMapping("/msg/code/send")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "用户手机号", dataType = "string", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto sendMobileMessageCode(@RequestParam String phone){
        return mobileMessageLogic.send(phone);
    }

    //修改昵称
    @ApiOperation(value = "用户修改昵称")
    @PostMapping("/rename")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personId", value = "用户ID", dataType = "string", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "nickname", value = "用户昵称", dataType = "string", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto rename(@RequestParam Integer personId, @RequestParam String nickname){
        return operateUserLogic.rename(personId, nickname);
    }

    //设置通知方式
    @ApiOperation(value = "用户设置通知方式")
    @PostMapping("/notify/set")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personId", value = "用户ID", dataType = "string", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "notifyId", value = "通知方式", dataType = "string", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto setNotify(@RequestParam Integer personId, @RequestParam Integer notifyId){
        return notifyUserLogic.set(personId, notifyId);
    }

    //添加一个短信通知方法
    @ApiOperation(value = "用户添加短信通知方式")
    @PostMapping("/notify/msg/add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personId", value = "用户ID", dataType = "string", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "phone", value = "用户手机号", dataType = "string", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto addMobileMessageCodeNotification(
            @RequestParam Integer personId,
            @RequestParam String phone,
            @RequestParam String code
    ){
        ResponseDto responseDto = mobileMessageLogic.verify(phone, code);

        if(responseDto.getCode() != ResponseDto.CODE_SUCCESS){
            return responseDto;
        }

        return notifyUserLogic.add(personId, phone, UserNotify.NOTIFY_TYPE_MSG);
    }

    //删除通知方法
    @ApiOperation(value = "用户删除通知方式")
    @PostMapping("/notify/remove")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personId", value = "用户ID", dataType = "string", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "notifyId", value = "通知方式", dataType = "string", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto removeNotification(@RequestParam Integer personId, @RequestParam Integer notifyId){
        return notifyUserLogic.remove(personId, notifyId);
    }

    //获得所有通知方式
    @ApiOperation(value = "用户获取通知方式")
    @PostMapping("/notify/get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personId", value = "用户ID", dataType = "string", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto getNotification(@RequestParam Integer personId){
        return notifyUserLogic.get(personId);
    }

    //获取机房
    @ApiOperation(value="用户获取管理机房")
    @PostMapping("/room/get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personId", value = "用户ID", dataType = "string", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto getRoom(@RequestParam Integer personId){ return userRoomLogic.GetRoom(personId); }
}
