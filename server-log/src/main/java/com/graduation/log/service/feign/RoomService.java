package com.graduation.log.service.feign;

import com.graduation.user.table.Room;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/system")
@FeignClient(name="user-service")
public interface RoomService {

    @PostMapping("/room/get")
    public List<Room> getRoom(@RequestParam Integer userId);

    @PostMapping("/room/getName")
    public String getRoomName(@RequestParam Integer roomId);
}
