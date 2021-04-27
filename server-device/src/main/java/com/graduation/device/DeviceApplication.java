package com.graduation.device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@EnableFeignClients
@SpringBootApplication
@EnableTransactionManagement
@EnableSwagger2
@MapperScan("com.graduation.device.mapper")
public class DeviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeviceApplication.class);
    }
}