package com.graduation.user.logic.user;

import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.user.service.user.UserService;
import com.graduation.user.table.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRoomLogic {
    @Autowired
    private UserService userService;

    //获取用户所管理的机房
    public ResponseDto GetRoom(Integer user_id){
        User user = userService.exist(user_id);
        if(user == null){
            return ResultUtils.error("用户ID传输错误");
        }
        return ResultUtils.success("查询成功", userService.view(user_id));
    }
}
