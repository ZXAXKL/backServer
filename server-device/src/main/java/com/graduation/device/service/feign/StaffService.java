package com.graduation.device.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RequestMapping("/system/staff")
@FeignClient(name="person-service")
public interface StaffService {

    //根据id列表查询员工信息
    @PostMapping("/list/info")
    public Map<Integer, Map<String, Object>> infoList(@RequestParam String idList, @RequestParam String... properties);

    //根据名字查询员工信息
    @PostMapping("/name/info")
    public Map<String, Object> info(@RequestParam String name, @RequestParam String... properties);

}

