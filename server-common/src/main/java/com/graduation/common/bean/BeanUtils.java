package com.graduation.common.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Configuration
public class BeanUtils implements ApplicationContextAware {

    private ConfigurableApplicationContext applicationContext;

    private ConfigurableBeanFactory configurableBeanFactory;

    //获取已注入Bean
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        this.configurableBeanFactory = this.applicationContext.getBeanFactory();
    }

    //动态注入Bean对象
    public void inject(String name, Object object){
        if(applicationContext.containsBean(name)){
            log.warn("已存在Bean" + name);
            return;
        }

        configurableBeanFactory.registerSingleton(name, object);
        log.info("成功注入Bean" + name);
    }

    //获取注入的Bean
    public Object getByName(String name){
        return applicationContext.getBean(name);
    }
}
