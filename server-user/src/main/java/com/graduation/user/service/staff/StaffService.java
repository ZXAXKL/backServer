package com.graduation.user.service.staff;

import com.graduation.user.mapper.StaffMapper;
import com.graduation.user.table.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class StaffService {

    @Autowired
    private StaffMapper staffMapper;

    //创建员工
    public void create(Staff staff){
        staffMapper.insertSelective(staff);
    }

    //改账号信息
    public Integer update(Integer staffId, String phone, String name){
        Staff staff = new Staff();
        staff.setId(staffId);
        staff.setName(name);
        staff.setPhone(phone);
        return staffMapper.updateByPrimaryKeySelective(staff);
    }

    //更换密码
    public void rekey(Integer staffId, String password){
        Staff staff = new Staff();
        staff.setId(staffId);
        staff.setPassword(password);
        staffMapper.updateByPrimaryKeySelective(staff);
    }

    //检查员工是否存在 -员工ID
    public Staff exist(Integer staffId){
        return staffMapper.selectByPrimaryKey(staffId);
    }

    //检查员工是否存在 -员工姓名
    public Staff exist(String name, String... properties) {
        Example example = new Example(Staff.class);
        if(properties.length > 0){
            example.selectProperties(properties);
        }
        example.createCriteria().andEqualTo("name", name);
        return staffMapper.selectOneByExample(example);
    }

    //检查员工是否存在 -员工电话号码
    public Staff exist(String phone){
        Staff staff = new Staff();
        staff.setPhone(phone);
        return staffMapper.selectOne(staff);
    }


    //条件查询员工信息
    public List<Staff> view(Map<String, Object> conditions, String... properties){
        Example example = new Example(Staff.class);
        //判断是否需要过滤字段
        if(properties.length > 0){
            example.selectProperties(properties);
        }
        //下面判断条件，如果有条件就进行筛选
        Example.Criteria criteria = example.createCriteria();
        //手机号前缀匹配
        Object value = conditions.get("phone");
        if(value != null){
            criteria.andLike("phone", value.toString() + "%");
        }
        //姓名前缀匹配
        value = conditions.get("name");
        if(value != null){
            criteria.andLike("name", value.toString() + "%");
        }
        //根据身份查询
        value = conditions.get("role");
        if(value != null){
            criteria.andEqualTo("role", value);
        }
        return staffMapper.selectByExample(example);
    }

    //根据id列表查询员工信息
    public List<Staff> view(Set<Integer> idList, String... properties){
        Example example = new Example(Staff.class);
        //根据传入的参数对结果进行过滤
        if(properties.length > 0){
            example.selectProperties(properties);
        }
        example.createCriteria().andIn("id", idList);
        //根据id列表查出的结果集里不会包含重复的项，并且是按id进行排序的
        return staffMapper.selectByExample(example);
    }

}
