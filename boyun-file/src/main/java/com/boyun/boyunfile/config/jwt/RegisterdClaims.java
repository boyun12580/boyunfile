package com.boyun.boyunfile.config.jwt;

import lombok.Data;

@Data
public class RegisterdClaims {
//    iss：发行人
//    exp：到期时间
//    sub：主题
//    aud：用户
//    nbf：在此之前不可用
//    iat：发布时间
//    jti：JWT ID用于标识该JWT

    private String iss;
    private String exp;
    private String sub;
    private String aud;
}