package com.boyun.boyunfile.config.jwt;

import lombok.Data;

@Data
public class JwtHeader {
//    标头通常由两部分组成：token 的类型（typ）和正在使用的签名算法（alg），如 HMAC SHA256 或 RSA。
    private String alg;
    private String typ;
}