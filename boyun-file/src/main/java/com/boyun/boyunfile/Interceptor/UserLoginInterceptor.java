package com.boyun.boyunfile.Interceptor;

import com.boyun.boyunfile.common.RestResult;
import com.boyun.boyunfile.constant.ResultCodeEnum;
import com.boyun.boyunfile.domain.User;
import com.boyun.boyunfile.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Intercepts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class UserLoginInterceptor implements HandlerInterceptor {

    @Resource
    JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取 http的header中获得 token信息
        String token=request.getHeader("token");

        User tokenUserInfo = null;
        try {

            Claims c = jwtUtil.parseJWT(token);
            String subject = c.getSubject();
            ObjectMapper objectMapper = new ObjectMapper();
            tokenUserInfo = objectMapper.readValue(subject, User.class);

        } catch (Exception e) {
            log.error("有未携带token请求发送");
            response.sendError(405, "认证失败");
            return false;
        }
        if (tokenUserInfo != null) {
            return true;
        } else {
            response.sendError(401, "用户暂未登录");
            return false;
        }
    }

    private void failToken(HttpServletResponse response, RestResult restResult) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(restResult);

        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
