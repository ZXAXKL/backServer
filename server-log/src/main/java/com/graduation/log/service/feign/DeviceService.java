package com.graduation.log.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/system")
@FeignClient(name="device-service")
public interface DeviceService {

    @PostMapping("/device/getName")
    public String getDeviceName(@RequestParam String sn);
}
