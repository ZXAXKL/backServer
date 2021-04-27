package com.graduation.user.service.user;

import com.graduation.user.mapper.ControlMapper;
import com.graduation.user.mapper.RoomMapper;
import com.graduation.user.mapper.UserMapper;
import com.graduation.user.table.Control;
import com.graduation.user.table.Room;
import com.graduation.user.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private ControlMapper controlMapper;

    //检查用户是否存在 - 用户ID
    public User exist(Integer userId, String... properties){
        Example example = new Example(User.class);
        //根据传入的参数对结果进行过滤
        if(properties.length > 0){
            example.selectProperties(properties);
        }
        example.createCriteria().andEqualTo("id", userId);
        return userMapper.selectOneByExample(example);
    }

    //检查用户是否存在 - 用户昵称
    public User exist(String nickname){
        User user = new User();
        user.setNickname(nickname);
        return userMapper.selectOne(user);
    }

    //创建用户
    public void create(User user){
        userMapper.insertSelective(user);
    }

    //检查授权记录
    public User check(Byte identityType, String openId){
        //查询授权记录
        User user = new User();
        user.setIdentityType(identityType);
        user.setOpenId(openId);
        return userMapper.selectOne(user);
    }

    //用户重命名
    public void rename(Integer userId, String nickname){
        User user = new User();
        user.setId(userId);
        user.setNickname(nickname);
        userMapper.updateByPrimaryKeySelective(user);
    }

    //设置用户的通知方式
    public void setNotifyMethod(Integer userId, Integer notifyId){
        //直接去更新
        User user = new User();
        user.setId(userId);
        user.setNotifyId(notifyId);

        userMapper.updateByPrimaryKeySelective(user);
    }

    //统计用户总数
    public Integer statisticUserSum(){
        return userMapper.selectCount(new User());
    }

    //查看用户列表
    public List<User> view(Map<String, Object> conditions, String... properties){
        Example example = new Example(User.class);
        //判断是否需要过滤字段
        if(properties.length > 0){
            example.selectProperties(properties);
        }
        //下面判断条件，如果有条件就进行筛选
        Example.Criteria criteria = example.createCriteria();
        //根据昵称模糊查询
        Object value = conditions.get("nickname");
        if(value != null){
            criteria.andLike("nickname", value.toString() + "%");
        }
        return userMapper.selectByExample(example);
    }

    //根据id列表查询用户信息
    public List<User> view(Set<Integer> idList, String... properties){
        if(idList.isEmpty()){
            return new ArrayList<>();
        }
        Example example = new Example(User.class);
        //根据传入的参数对结果进行过滤
        if(properties.length > 0){
            example.selectProperties(properties);
        }
        example.createCriteria().andIn("id", idList);
        //根据id列表查出的结果集里不会包含重复的项，并且是按id进行排序的
        return userMapper.selectByExample(example);
    }

    //根据id返回用户管理的机房
    public List<Room> view(Integer user_id){
        List<Control> tempControl;
        Example example = new Example(Control.class);
        example.createCriteria().andEqualTo("user_id", user_id);
        tempControl = controlMapper.selectByExample(example);
        List<Room> room = new ArrayList<Room>();
        for(Control control: tempControl){
            room.add(roomMapper.selectByPrimaryKey(control.getRoom_id()));
        }
        return room;
    }

    //根据id返回机房名称
    public String getName(Integer room_id){
        Example example = new Example(Room.class);
        example.createCriteria().andEqualTo("id", room_id);
        return roomMapper.selectByPrimaryKey(example).getName();
    }
}

