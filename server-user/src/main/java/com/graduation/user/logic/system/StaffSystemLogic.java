package com.graduation.user.logic.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.graduation.common.format.FormatUtils;
import com.graduation.user.table.Staff;
import com.graduation.user.service.staff.StaffService;

import java.util.*;

@Component
public class StaffSystemLogic {

    @Autowired
    private StaffService staffService;

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
        //调下层接口获取员工列表
        List<Staff> staffs = staffService.view(idList, strs.toArray(new String[0]));
        Map<Integer, Map<String, Object>> result = new HashMap<>();
        //把集合中的user转成Map
        for(Staff staff : staffs){
            result.put(staff.getId(), FormatUtils.objectToMap(staff));
        }
        //返回集合
        return result;
    }

    //根据名字查询数据
    public Map<String, Object> info(String name, String... properties) throws Exception {
        return FormatUtils.objectToMap(staffService.exist(name, properties));
    }

}
