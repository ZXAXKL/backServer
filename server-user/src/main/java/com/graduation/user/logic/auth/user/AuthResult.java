package com.graduation.user.logic.auth.user;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class AuthResult {

    //校验结果
    private Boolean result;
    //第三方平台openid
    private String openId;
    //第三方平台签发token
    private String token;
    //第三方平台用户信息
    private JSONObject info;

}
