package com.graduation.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.graduation.user.table.User;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVoToAdmin {

    private Integer id;

    private String nickname;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Boolean disable;

    private String client;

    public UserVoToAdmin(User user, Map<Integer, User> users){
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.createTime = user.getCreateTime();
    }

}

