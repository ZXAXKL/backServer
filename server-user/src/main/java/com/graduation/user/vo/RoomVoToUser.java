package com.graduation.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.graduation.user.table.Room;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomVoToUser {

    private Integer id;

    private String name;

    private String state;

    private String detail;

    public RoomVoToUser(Room room, Map<Integer, Room> rooms){
        this.id = room.getId();
        this.name = room.getName();
        this.state = room.getState();
        this.detail = room.getDetail();
    }
}
