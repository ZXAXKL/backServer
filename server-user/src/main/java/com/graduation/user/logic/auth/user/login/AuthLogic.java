package com.graduation.user.logic.auth.user.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.user.logic.auth.user.AuthResult;
import com.graduation.user.table.User;
import com.graduation.user.service.user.UserNotifyService;
import com.graduation.user.service.user.UserService;
import com.graduation.user.utils.token.TokenUtils;
import com.graduation.user.vo.UserVoToUser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AuthLogic {

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserNotifyService userNotifyService;

    @Autowired
    private TokenUtils tokenUtils;

    //认证，并获取用户信息
    protected abstract AuthResult auth(Map<String, Object> map);

    //表示验证的类型
    protected abstract Byte authType();

    //可以用来设置用户通知方式,返回设置的id,内部调用
    protected abstract Integer sign(Integer userId, AuthResult authResult);

    //对外开放一个登陆接口
    @Transactional
    public ResponseDto login(Map<String, Object> map){
        AuthResult authResult = auth(map);

        if(!authResult.getResult()){
            return ResultUtils.error("校验不通过");
        }

        //确认用户是否是第一次登陆
        User user = userService.check(authType(), authResult.getOpenId());

        //是第一次登陆就生成用户数据
        if(user == null){
            //生成用户对象并插入数据库
            user = new User();
            user.setIdentityType(authType());
            user.setOpenId(authResult.getOpenId());

            //虽然短token重复率很低，但是还是以防万一
            while(true){
                try {
                    user.setNickname(tokenUtils.genShortToken());
                    userService.create(user);
                    break;
                } catch (DuplicateKeyException ignored){
                }
            }
            //给用户设置通知方式
            user.setNotifyId(sign(user.getId(), authResult));
        }

        //包装信息
        Map<String, Object> info = new HashMap<>();
        //存openId
        info.put("openId", user.getOpenId());
        //用户名字
        info.put("name", user.getNickname());

        //存redis
        String token =  tokenUtils.putInRedis(info, user.getId());

        info.clear();

        UserVoToUser userVoToUser = new UserVoToUser(user);
        userVoToUser.setNotifyList(Collections.singletonList(userNotifyService.exist(user.getNotifyId())));

        info.put("user", userVoToUser);
        info.put("token", token);
        return ResultUtils.success("登陆成功", info);
    }

}
