package com.graduation.user.logic.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.graduation.user.table.User;
import com.graduation.user.vo.UserVoToAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.user.utils.token.TokenUtils;
import com.graduation.user.service.user.UserService;

import java.util.*;

@Component
public class UserAdminLogic {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtils tokenUtils;

    //分页查看用户信息列表
    public ResponseDto view(Integer pageNum, Integer pageSize, Map<String, Object> conditions){
        PageHelper.startPage(pageNum,pageSize);
        List<User> users = userService.view(conditions, "id", "nickname", "createTime");
        PageInfo<User> pageInfo = new PageInfo<User>(users);

        Set<Integer> set = new HashSet<>();
        for(User user : users){
            set.add(user.getId());
        }

        List<User> clients = userService.view(set, "id", "nickname");
        Map<Integer, User> map = new HashMap<>();
        for(User client : clients){
            map.put(client.getId(), client);
        }

        List<UserVoToAdmin> result = new ArrayList<>();
        for(User user : users){
            result.add(new UserVoToAdmin(user, map));
        }

        conditions.clear();
        conditions.put("list", result);
        conditions.put("pages", pageInfo.getPages());
        conditions.put("total", pageInfo.getTotal());
        return ResultUtils.success("设备信息模糊查询完毕", conditions);
    }

}

