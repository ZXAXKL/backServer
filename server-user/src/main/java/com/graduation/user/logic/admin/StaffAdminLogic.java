package com.graduation.user.logic.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import com.graduation.common.format.FormatUtils;
import com.graduation.common.result.ResponseDto;
import com.graduation.common.result.ResultUtils;
import com.graduation.user.table.Staff;
import com.graduation.user.service.staff.StaffService;
import com.graduation.user.utils.token.TokenUtils;
import com.graduation.user.vo.StaffVo;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StaffAdminLogic {

    @Autowired
    private StaffService staffService;

    @Autowired
    private TokenUtils tokenUtils;

    //修改指定员工的账号信息
    public ResponseDto update(Integer staffId, String phone, String name){
        //检查手机号
        if(phone != null && !FormatUtils.checkCellphone(phone)){
            return ResultUtils.warning("手机号格式错误");
        }

        try{
            //检查存在性
            Staff staff = staffService.exist(staffId);
            if(staff == null){
                return ResultUtils.warning("账号不存在");
            }
            //改数据库
            staffService.update(staffId, phone, name);
            //改token
            if(name != null){
                tokenUtils.modify(staffId, "name", name);
            }
            return ResultUtils.success("修改成功");

        } catch (DuplicateKeyException e){
            //若出现重复异常判断一下原因
            String[] strs = e.getCause().getMessage().split("'");
            if(strs[strs.length - 1].equals("phone_un")){
                return ResultUtils.error("该手机号码已经存在");
            } else{
                return ResultUtils.error("该名字的员工已经存在");
            }
        }
    }

    //重置员工密码
    public ResponseDto resetPassword(Integer staffId) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //获取员工信息
        Staff staff = staffService.exist(staffId);
        //检查员工是否存在
        if(staff == null){
            return ResultUtils.warning("该员工不存在");
        }
        //重置员工密码为手机号
        staffService.rekey(staffId, FormatUtils.encodePwd(staff.getPhone()));
        return ResultUtils.success("重置成功");
    }


    //分页查看员工信息列表
    public ResponseDto view(Integer pageNum, Integer pageSize, Map<String, Object> conditions){
        PageHelper.startPage(pageNum,pageSize);
        List<Staff> staffs = staffService.view(conditions, "id", "phone", "name", "role", "disable");
        PageInfo<Staff> pageInfo = new PageInfo<Staff>(staffs);
        conditions.clear();
        conditions.put("list", pageInfo.getList());
        conditions.put("pages", pageInfo.getPages());
        conditions.put("total", pageInfo.getTotal());
        return ResultUtils.success("设备信息模糊查询完毕", conditions);
    }

    //创建用户
    public ResponseDto create(String phone, String name) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //检查手机号
        if(!FormatUtils.checkCellphone(phone)){
            return ResultUtils.warning("手机号格式错误");
        }

        //创建用户
        Staff staff = new Staff();
        staff.setPhone(phone);
        //初始密码为电话号码
        staff.setPassword(FormatUtils.encodePwd(phone));
        staff.setName(name);

        try{
            staffService.create(staff);
            return ResultUtils.success("创建成功", new StaffVo(staff));
        } catch (DuplicateKeyException e){
            //若出现重复异常判断一下原因
            String[] strs = e.getCause().getMessage().split("'");
            if(strs[strs.length - 1].equals("phone_un")){
                return ResultUtils.error("该手机号码对应的员工已经录入");
            } else{
                return ResultUtils.error("该名字的员工已经录入");
            }
        }
    }

}
