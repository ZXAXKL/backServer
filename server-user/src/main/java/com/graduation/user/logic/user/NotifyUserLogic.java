package com.graduation.user.logic.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.user.table.UserNotify;
import com.graduation.user.service.user.UserNotifyService;
import com.graduation.user.service.user.UserService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class NotifyUserLogic {

    @Autowired
    private UserNotifyService userNotifyService;

    @Autowired
    private UserService userService;

    //设置通知方式
    public ResponseDto set(Integer userId, Integer notifyId){
        //notifyId为0表示不选中任何通知选项，如果为其他值表明要选中一个选项，需要检查存在性
        if(notifyId != 0){
            //获取对应的信息
            UserNotify userNotify = userNotifyService.exist(notifyId);
            //如果不存在，或者不属于当前用户，则报错
            if(userNotify == null || !userNotify.getUserId().equals(userId)){
                return ResultUtils.error("该选项不存在");
            }
        }
        userService.setNotifyMethod(userId, notifyId);
        return ResultUtils.success("设置成功");
    }

    //添加通知方式
    public ResponseDto add(Integer userId, String target, Byte notifyType){
        UserNotify userNotify = new UserNotify();
        userNotify.setUserId(userId);
        userNotify.setTarget(target);
        userNotify.setNotifyType(notifyType);

        userNotifyService.add(userNotify);
        return ResultUtils.success("添加成功", userNotify);
    }

    //删除通知方式
    public ResponseDto remove(Integer userId, Integer notifyId){
        if(userNotifyService.delete(userId, notifyId) == 0){
            return ResultUtils.warning("该记录不存在");
        } else {
            return ResultUtils.success("删除成功");
        }
    }

    //获取用户的所有通知方式
    public ResponseDto get(Integer userId){
        Map<String, Object> map = new HashMap<>();
        map.put("select", userService.exist(userId).getNotifyId());
        map.put("list", userNotifyService.view(userId, "id", "target", "notifyType"));
        return ResultUtils.success("获取成功", map);
    }

}
