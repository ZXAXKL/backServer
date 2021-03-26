package com.graduation.user.utils.wx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class WxConfig {

    @Value("${wechat.mini-program.app-id}")
    public String APP_ID;

    @Value("${wechat.mini-program.app-secret}")
    public String APP_SECRET;

}
