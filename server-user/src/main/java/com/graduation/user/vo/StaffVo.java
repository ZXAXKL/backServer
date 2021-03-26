package com.graduation.user.vo;

import lombok.Data;
import com.graduation.user.table.Staff;

@Data
public class StaffVo {

    private Integer id;

    private String phone;

    private String name;

    private Byte role;

    private Boolean disable;

    public StaffVo(Staff staff){
        this.id = staff.getId();
        this.phone = staff.getPhone();
        this.name = staff.getName();
    }

}
