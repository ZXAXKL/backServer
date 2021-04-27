package com.graduation.log.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.graduation.log.table.DeviceWarnLog;
import com.graduation.log.table.WarnLogTransfer;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarnLogVoToUser {
    private Long id;

    private Integer roomId;

    private String sn;

    private Byte type;

    private Integer userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Integer confirmId;

    private String info;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmTime;

    private String roomName;

    private String deviceName;

    public WarnLogVoToUser(WarnLogTransfer warnLogTransfer){
        this.id = warnLogTransfer.getId();
        this.roomId = warnLogTransfer.getRoomId();
        this.sn = warnLogTransfer.getSn();
        this.createTime = warnLogTransfer.getCreateDate();
        this.confirmId = warnLogTransfer.getConfirmId();
        this.confirmTime = warnLogTransfer.getConfirmDate();
        this.info = warnLogTransfer.getInfo();
        this.roomName = warnLogTransfer.getRoomName();
        this.deviceName = warnLogTransfer.getDeviceName();
    }
}
