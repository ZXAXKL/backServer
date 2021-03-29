package com.graduation.user.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.graduation.common.result.ResponseDto;
import com.graduation.user.logic.auth.staff.StaffLogic;
import com.graduation.user.logic.admin.StaffAdminLogic;
import com.graduation.user.logic.system.MobileMessageLogic;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = {"管理员接口"})
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private StaffLogic adminLogic;

    @Autowired
    private StaffAdminLogic staffAdminLogic;

    @Autowired
    private MobileMessageLogic mobileMessageLogic;

    //账号密码登陆
    @ApiOperation(value="管理员密码登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "管理员手机号", dataType = "string", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "password", value = "管理员密码", dataType = "string", paramType = "query", required = true, defaultValue = "")
    })
    @PostMapping("/password/login")
    public ResponseDto loginByPwd(
            @RequestParam String phone,
            @RequestParam String password
    ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return adminLogic.login(phone, password);
    }

    //发验证码
    @ApiOperation(value="管理员发送验证码")
    @RequestMapping("/code/send")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "管理员手机号", dataType = "string", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto sendMobileMessageCode(@RequestParam String phone){
        return mobileMessageLogic.send(phone);
    }

    //验证码登陆
    @ApiOperation(value = "管理员验证码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "管理员手机号", dataType = "string", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "code", value = "管理员验证码", dataType = "string", paramType = "query", required = true, defaultValue = "")
    })
    @RequestMapping("/code/login")
    public ResponseDto loginByCode(
            @RequestParam String phone,
            @RequestParam String code
    ) {
        ResponseDto responseDto = mobileMessageLogic.verify(phone, code);

        if(responseDto.getCode() != ResponseDto.CODE_SUCCESS){
            return responseDto;
        }

        return adminLogic.login(phone);
    }

    //改密码
    @ApiOperation(value = "管理员修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personId", value = "管理员ID", dataType = "string", paramType = "header", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "password", value = "管理员密码", dataType = "string", paramType = "query", required = true, defaultValue = "")
    })
    @PostMapping("/rekey")
    public ResponseDto rekey(@RequestHeader Integer personId, @RequestParam String password) throws Exception {
        return adminLogic.rekey(personId, password);
    }

    //创建员工
    @ApiOperation(value = "管理员创建员工")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "员工手机号", dataType = "string", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "name", value = "员工姓名", dataType = "string", paramType = "query", required = true, defaultValue = "")
    })
    @PostMapping("/staff/create")
    public ResponseDto createStaff(
            @RequestParam String phone,
            @RequestParam String name
    ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return staffAdminLogic.create(phone, name);
    }

    //条件查询员工列表
    @ApiOperation(value = "管理员查询员工列表")
    @PostMapping("/staff/view")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "分页数量", dataType = "Integer", paramType = "query", required = true, defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "Integer", paramType = "query", required = true, defaultValue = "10"),
            @ApiImplicitParam(name = "phone", value = "员工手机号", dataType = "string", paramType = "query", required = false, defaultValue = ""),
            @ApiImplicitParam(name = "name", value = "员工姓名", dataType = "string", paramType = "query", required = false, defaultValue = "")
    })
    public ResponseDto viewStaff(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String name
    ){
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("phone", phone);
        conditions.put("name", name);
        return staffAdminLogic.view(pageNum, pageSize, conditions);
    }

    //修改员工数据
    @ApiOperation(value = "管理员修改员工信息")
    @PostMapping("/staff/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "staffId", value = "员工ID", dataType = "Integer", paramType = "query", required = true, defaultValue = ""),
            @ApiImplicitParam(name = "phone", value = "员工手机号", dataType = "string", paramType = "query", required = false, defaultValue = ""),
            @ApiImplicitParam(name = "name", value = "员工姓名", dataType = "string", paramType = "query", required = false, defaultValue = "")
    })
    public ResponseDto updateStaff(
            @RequestParam Integer staffId,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String name
    ){
        return staffAdminLogic.update(staffId, phone, name);
    }

    //重置员工密码
    @ApiOperation(value = "管理员重置员工密码")
    @PostMapping("/staff/password/reset")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "staffId", value = "员工ID", dataType = "Integer", paramType = "query", required = true, defaultValue = "")
    })
    public ResponseDto resetPasswordStaff(@RequestParam Integer staffId) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return staffAdminLogic.resetPassword(staffId);
    }
}
