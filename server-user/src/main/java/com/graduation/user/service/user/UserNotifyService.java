package com.graduation.user.service.user;

import com.graduation.user.mapper.UserNotifyMapper;
import com.graduation.user.table.UserNotify;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserNotifyService {

    @Autowired
    private UserNotifyMapper userNotifyMapper;

    //获取用户所有通知方式
    public List<UserNotify> view(Integer userId, String... properties){
        Example example = new Example(UserNotify.class);
        //过滤出指定字段
        if(properties.length > 0){
            example.selectProperties(properties);
        }
        //根据用户id锁定行
        example.createCriteria().andEqualTo("userId", userId);
        return userNotifyMapper.selectByExample(example);
    }

    //检查是否存在
    public UserNotify exist(Integer id){
        return userNotifyMapper.selectByPrimaryKey(id);
    }

    //添加通知方式
    public void add(UserNotify userNotify){
        userNotifyMapper.insertSelective(userNotify);
    }

    //删除通知方式
    public Integer delete(Integer userId, Integer notifyId){
        UserNotify userNotify = new UserNotify();
        userNotify.setUserId(userId);
        userNotify.setId(notifyId);

        return userNotifyMapper.delete(userNotify);
    }

}
