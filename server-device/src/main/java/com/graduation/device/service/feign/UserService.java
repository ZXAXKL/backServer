package com.graduation.device.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RequestMapping("/system/user")
@FeignClient(name="person-service")
public interface UserService {

    //根据id列表查询用户信息
    @PostMapping("/list/info")
    public Map<Integer, Map<String, Object>> infoList(@RequestParam String idList, @RequestParam String... properties);

    //根据名称查询用户数据
    @PostMapping("/name/info")
    public Map<String, Object> info(@RequestParam String nickname,@RequestParam String... properties);

}
