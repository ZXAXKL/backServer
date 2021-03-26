package com.graduation.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import com.graduation.user.table.User;
import com.graduation.user.table.UserNotify;

import java.util.List;

@Data

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVoToUser {

    private Integer id;

    private String openId;

    private String nickname;

    private Integer notifyId;

    private Integer client;

    private List<UserNotify> notifyList;

    public UserVoToUser(User user){
        this.id = user.getId();
        this.openId = user.getOpenId();
        this.nickname = user.getNickname();
        this.notifyId = user.getNotifyId();
    }

}

