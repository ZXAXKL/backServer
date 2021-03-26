package com.graduation.user.logic.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.user.service.user.UserService;
import com.graduation.user.utils.token.TokenUtils;

@Component
public class OperateUserLogic {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtils tokenUtils;

    //修改用户名称
    public ResponseDto rename(Integer userId, String nickname){
        try{
            //修改名字
            userService.rename(userId, nickname);
            //成功之后，修改token
            tokenUtils.modify(userId, "name", nickname);
        } catch (DuplicateKeyException ignored){
            return ResultUtils.warning("该昵称已经存在");
        }

        return ResultUtils.success("改名成功");
    }

}