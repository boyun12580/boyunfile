package com.boyun.boyunfile.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.boyun.boyunfile.util.PropertiesUtil;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class PropertiesConfig {

    @Resource
    private Environment env;

//    初始化，类注入后自动调用
    @PostConstruct
    public void setProperties() {
        PropertiesUtil.setEnvironment(env);
    }

//    public Environment getEnv(){
//        return this.env;
//    }

}