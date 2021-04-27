package com.graduation.user.table;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "control_table")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Control {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "room_id")
    private Integer room_id;
}
