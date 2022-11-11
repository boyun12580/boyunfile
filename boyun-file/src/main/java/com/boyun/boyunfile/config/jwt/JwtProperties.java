package com.boyun.boyunfile.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component

//读取 application.properties 配置文件中以 jwt 开头的配置
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private JwtHeader header;
    private JwtPayload payload;
}