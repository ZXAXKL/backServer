package com.graduation.user.web;

import com.graduation.user.service.user.UserService;
import com.graduation.user.table.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//提供其他服务的接口
@Slf4j
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    UserService userService;

    @PostMapping("/room/get")
    public List<Room> getRoom(@RequestParam Integer userId){
        return userService.view(userId);
    }
}
