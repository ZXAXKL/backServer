package com.graduation.device.table;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "device_table")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device {

    @Id
    @Column(name = "id")
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "state")
    private Integer state;

    @Column(name = "room_id")
    private Integer room_id;
}
