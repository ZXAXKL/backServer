package com.graduation.log.table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Table(name = "warn_log")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceWarnLog {

    public static final Byte TYPE_OFF_LINE = 0;

    public static final Byte TYPE_VOLT = 1;

    public static final Byte TYPE_CURRENT = 2;

    public static final Byte TYPE_INIT_FAIL = 3;

    public static final Byte TYPE_STOP = 4;

    //把命令映射到数字
    public static Map<String, Byte> typeMap = new HashMap<String, Byte>(){{

        put("offline", TYPE_OFF_LINE);

        put("v_warn", TYPE_VOLT);

        put("c_warn", TYPE_CURRENT);

        put("initfail", TYPE_INIT_FAIL);

        put("m_stop", TYPE_STOP);

    }};

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "sn")
    private String sn;

    @Column(name = "info")
    private String info;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @Column(name = "confirm_id")
    private Integer confirmId;

    @Column(name = "confirm_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmDate;
}
