package com.graduation.user.table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "user_table")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    public static final Byte AUTH_TYPE_WECHAT = 0;

    @Id
    @Column(name = "id")
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    @Column(name = "identity_type")
    private Byte identityType;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "nickname")
    private String nickname;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Column(name = "notify_id")
    private Integer notifyId;

}
