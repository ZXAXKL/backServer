package com.graduation.user.logic.auth.user.login.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.graduation.common.format.FormatUtils;
import com.graduation.user.table.User;
import com.graduation.user.table.UserNotify;
import com.graduation.user.logic.auth.user.AuthResult;
import com.graduation.user.logic.auth.user.login.AuthLogic;
import com.graduation.user.utils.wx.WxUtils;

import java.util.Map;

@Slf4j
@Component
public class WeChatLogin extends AuthLogic {

    @Autowired
    private WxUtils wxUtils;

    @Override
    protected AuthResult auth(Map<String, Object> map) {
        //new一个结果对象
        AuthResult authResult = new AuthResult();
        //默认校验成功
        authResult.setResult(true);

        //小程序发送的code
        String code = map.get("code").toString();
        // 用户非敏感信息：rawData
        String rawDataStr = map.get("rawData").toString();
        //检查格式，如果错误直接返回
        if(!FormatUtils.isJSON( rawDataStr)){
            authResult.setResult(false);
            return authResult;
        }
        JSONObject rawData = JSONObject.parseObject(rawDataStr);
        String signature = map.get("signature").toString();

        // 获取openid和session_key
        JSONObject SessionKeyOpenId = wxUtils.getSessionKeyAndOpenId(code);
        String openid = SessionKeyOpenId.getString("openid");
        String sessionKey = SessionKeyOpenId.getString("session_key");
        //生成一个签名
        String signature2 = DigestUtils.sha1Hex(rawDataStr + sessionKey);
        //检查小程序的签名和生成的签名是否吻合,不吻合说明不是本平台的小程序
        if (!signature.equals(signature2)) {
            authResult.setResult(false);
            return authResult;
        }

        //检查是否包含加密信息
        Object encryptedData = map.get("encryptedData");
        Object iv = map.get("iv");

        //如果有就解密出来并放进用户信息里
        if(encryptedData != null && iv != null){
            JSONObject encryptedDataObject = wxUtils.getUserInfo(encryptedData.toString(), sessionKey, iv.toString());
            rawData.put("phone", encryptedDataObject.get("phoneNumber"));
        }

        //写入获取的信息
        authResult.setOpenId(openid);
        authResult.setToken(code);
        authResult.setInfo(rawData);

        //返回结果
        return authResult;
    }

    @Override
    protected Byte authType() {
        return User.AUTH_TYPE_WECHAT;
    }

    @Override
    protected Integer sign(Integer userId, AuthResult authResult) {
        //获取手机号码
        String phone = authResult.getInfo().getString("phone");
        UserNotify userNotify = new UserNotify();
        //如果有手机号码就设置为默认联系方式
        if(phone != null){
            //生成通知方法记录并插入数据库
            userNotify.setUserId(userId);
            userNotify.setNotifyType(UserNotify.NOTIFY_TYPE_MSG);
            userNotify.setTarget(phone);

            userNotifyService.add(userNotify);

            //设置默认的联系方式
            userService.setNotifyMethod(userId, userNotify.getId());
        }
        return userNotify.getId();
    }

}

