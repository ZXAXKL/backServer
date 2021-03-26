package com.graduation.common;

import com.mangofactory.swagger.plugin.EnableSwagger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableSwagger
@EnableScheduling
@EnableTransactionManagement
@ComponentScan(basePackages = "com.graduation.common")
public class SwaggerConfiguration {

}
