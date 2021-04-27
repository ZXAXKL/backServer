package com.graduation.log.table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "operation_log")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperationLog {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "info")
    private String info;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
