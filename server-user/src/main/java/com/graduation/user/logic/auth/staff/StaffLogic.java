package com.graduation.user.logic.auth.staff;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.graduation.common.format.FormatUtils;
import com.graduation.common.redis.RedisHashUtils;
import com.graduation.common.redis.RedisUtils;
import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.user.logic.system.MobileMessageLogic;
import com.graduation.user.table.Staff;
import com.graduation.user.service.staff.StaffService;
import com.graduation.user.utils.token.TokenUtils;
import com.graduation.user.vo.StaffVo;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public abstract class StaffLogic {

    @Autowired
    protected StaffService staffService;

    @Autowired
    protected MobileMessageLogic mobileMessageLogic;

    @Autowired
    private TokenUtils tokenUtils;

    protected abstract Byte roleType();

    protected abstract String roleStr();

    //检查员工是否能登陆
    public ResponseDto check(String phone){
        //检查是否有这个员工
        Staff staff = staffService.exist(phone);
        //检查账号是否存在
        if(staff == null){
            return ResultUtils.warning("账号不存在");
        }
        return ResultUtils.success("", staff);
    }

    //签发token
    public ResponseDto allow(Staff staff){

        Map<String, Object> info = new HashMap<>();
        info.put("name", staff.getName());

        //存redis
        String token = tokenUtils.putInRedis(info,  staff.getId());

        info.clear();

        StaffVo staffVo = new StaffVo(staff);

        info.put("staff", staffVo);
        info.put("token", token);
        return ResultUtils.success("登陆成功", info);
    }

    //密码登陆
    public final ResponseDto login(String phone, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //检查员工是否能登陆
        ResponseDto responseDto = check(phone);
        //不能登陆直接返回
        if(responseDto.getCode() != ResponseDto.CODE_SUCCESS){
            return responseDto;
        }

        Staff staff = (Staff) responseDto.getPayload();

        //检查密码是否正确
        if(!staff.getPassword().equals(FormatUtils.encodePwd(password))){
            return ResultUtils.error("密码错误");
        }
        //校验通过
        return allow(staff);
    }

     //用于验证码检验通过登陆,不包含验证码检验
    public final ResponseDto login(String phone) {
        //检查员工是否能登陆
        ResponseDto responseDto = check(phone);
        //不能登陆直接返回
        if(responseDto.getCode() != ResponseDto.CODE_SUCCESS){
            return responseDto;
        }

        //校验通过
        return allow((Staff) responseDto.getPayload());
    }

    //改变密码
    public ResponseDto rekey(Integer staffId, String password) throws Exception {
        //调用这个方法说明这个员工肯定存在，不需要检查，直接修改数据库
        staffService.rekey(staffId, FormatUtils.encodePwd(password));
        return ResultUtils.success("修改密码成功");
    }

}
