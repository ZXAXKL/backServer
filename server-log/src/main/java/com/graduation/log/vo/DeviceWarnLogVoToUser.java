package com.graduation.log.vo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.graduation.log.table.DeviceWarnLog;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceWarnLogVoToUser {

    //该数组用于查询数据库，只缺少数据部分，因为这部分对用户不可见
    //public static final String[] columns = { "id", "roomId", "sn", "name", "createTime", "isRead" };

    private Long id;

    private Integer roomId;

    private String sn;

    private Byte type;

    private Integer userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Integer confirmId;

    private JSONObject info;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmTime;

    public DeviceWarnLogVoToUser(DeviceWarnLog deviceWarnLog){
        this.id = deviceWarnLog.getId();
        this.roomId = deviceWarnLog.getRoomId();
        this.sn = deviceWarnLog.getSn();
        this.createTime = deviceWarnLog.getCreateDate();
        this.confirmId = deviceWarnLog.getConfirmId();
        this.confirmTime = deviceWarnLog.getConfirmDate();
        this.info = JSONObject.parseObject(deviceWarnLog.getInfo());
    }

}