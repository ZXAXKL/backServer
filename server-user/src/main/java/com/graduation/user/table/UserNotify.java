package com.graduation.user.table;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "user_notify")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserNotify {

    public static final Byte NOTIFY_TYPE_MSG = 0;

    @Id
    @Column(name = "id")
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "target")
    private String target;

    @Column(name = "notify_type")
    private Byte notifyType;

}

