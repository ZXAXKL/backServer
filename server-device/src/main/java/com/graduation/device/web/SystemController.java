package com.graduation.device.web;

import com.graduation.device.service.device.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    DeviceService deviceService;

    @PostMapping("/device/getName")
    public String getDeviceName(@RequestParam String sn){ return deviceService.getName(sn);}
}