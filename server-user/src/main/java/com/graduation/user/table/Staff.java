package com.graduation.user.table;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "staff_table")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Staff {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

}

