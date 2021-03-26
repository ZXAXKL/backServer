package com.graduation.user.logic.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.graduation.common.format.FormatUtils;
import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.user.table.User;
import com.graduation.user.service.user.UserNotifyService;
import com.graduation.user.service.user.UserService;
import com.graduation.user.utils.token.TokenUtils;
import com.graduation.user.vo.UserVoToUser;

import java.util.*;

@Slf4j
@Component
public class UserSystemLogic {

    @Autowired
    private UserService userService;

    //查询数据
    public Map<Integer, Map<String, Object>> info(Set<Integer> idList, String... properties) throws Exception {
        //判断为空
        if(idList.isEmpty()){
            return new HashMap<>();
        }
        //如果需要使用结果集过滤，必须保证过滤条件中有id，因为需要建立id和用户的映射
        //检查一下参数列表，如果没包含id，就加入id，保证给下层的过滤条件存在id
        List<String> strs = new ArrayList<>(Arrays.asList(properties));
        if(properties.length > 0 && !strs.contains("id")){
            strs.add("id");
        }
        //调下层接口获取用户列表
        List<User> users = userService.view(idList, strs.toArray(new String[0]));
        Map<Integer, Map<String, Object>> result = new HashMap<>();
        //把集合中的user转成Map
        for(User user : users){
            result.put(user.getId(), FormatUtils.objectToMap(user));
        }
        //返回集合
        return result;
    }

    //根据用户名查询用户
    public Map<String, Object> info(String nickname, String... properties) throws Exception {
        return FormatUtils.objectToMap(userService.exist(nickname));
    }

    @Autowired
    private UserNotifyService userNotifyService;

    @Autowired
    private TokenUtils tokenUtils;


    //用于测试的授权
    public ResponseDto testAuth() {
        //获取用户数据
        User user = userService.exist(1);
        //包装信息
        Map<String, Object> info = new HashMap<>();
        info.put("proxy", user.getId());
        info.put("openId", user.getOpenId());
        info.put("name", user.getNickname());

        //存redis
        String token = tokenUtils.putInRedis(info, user.getId());

        info.clear();

        UserVoToUser userVoToUser = new UserVoToUser(user);
        userVoToUser.setNotifyList(Collections.singletonList(userNotifyService.exist(user.getNotifyId())));

        info.put("user", userVoToUser);
        info.put("token", token);
        return ResultUtils.success("登陆成功", info);
    }

}
