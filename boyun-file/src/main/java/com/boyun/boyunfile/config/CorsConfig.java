package com.boyun.boyunfile.config;

import com.boyun.boyunfile.Interceptor.UserLoginInterceptor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Slf4j
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Resource
    UserLoginInterceptor userLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/**")
                .excludePathPatterns("/filetransfer/downloadfile")
                .excludePathPatterns("/upload/**")
                .excludePathPatterns("/share/**");
    }
}
