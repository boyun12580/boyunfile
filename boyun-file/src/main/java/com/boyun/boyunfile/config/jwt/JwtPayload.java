package com.boyun.boyunfile.config.jwt;

import lombok.Data;

@Data
public class JwtPayload {
//    token 的第二部分是有效负载，其中包含 claims。claims 是关于实体（通常为用户）和其他数据的语句。
    private RegisterdClaims registerdClaims;
}