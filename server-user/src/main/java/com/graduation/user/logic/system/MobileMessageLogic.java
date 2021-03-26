package com.graduation.user.logic.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.graduation.common.redis.RedisUtils;
import com.graduation.common.redis.sn.SerialNumberUtils;
import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.user.utils.notify.MobileMessageUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MobileMessageLogic {

    @Autowired
    private SerialNumberUtils serialNumberUtils;

    public static final String KEY_PREFIX = "mobile_msg_code_";

    public ResponseDto send(String phone){

        String key = KEY_PREFIX + phone;

        //检查一下上一条验证码是否过期
        if(RedisUtils.get(key) != null){
            return ResultUtils.warning("旧的验证码还没有过期");
        }

        //检查是否达到发送上限
        Long sn = serialNumberUtils.getSn(key);

        if(sn != null && sn > 20){
            return ResultUtils.warning("验证码一天最多发送20条");
        }

        //生成一个六位的验证码
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < 6; ++i){
            stringBuilder.append((int) (Math.random() * 10));
        }

        String code = stringBuilder.toString();

        //发验证码
        switch (MobileMessageUtils.sendCodeMsg(phone, code).getCode()){
            case "isv.MOBILE_NUMBER_ILLEGAL":
                return ResultUtils.error("手机号码错误");
            case "isv.BUSINESS_LIMIT_CONTROL":
                return ResultUtils.error("发送频率过快");
        }

        //验证码存redis并且发送次数自增
        RedisUtils.put(key, code, 5L, TimeUnit.MINUTES);
        serialNumberUtils.incrAndGetSn(key);

        return ResultUtils.success("发送成功");
    }

    //判断验证码是否正确
    public ResponseDto verify(String phone, String code){
        Object value = RedisUtils.get(KEY_PREFIX + phone);
        if(value != null && value.toString().equals(code)){
            return ResultUtils.success("验证码正确");
        } else {
            return ResultUtils.error("验证码错误");
        }
    }

}
