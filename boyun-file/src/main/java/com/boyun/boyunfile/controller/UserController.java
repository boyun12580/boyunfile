package com.boyun.boyunfile.controller;

import com.boyun.boyunfile.common.RestResult;
import com.boyun.boyunfile.domain.User;
import com.boyun.boyunfile.dto.RegisterDTO;
import com.boyun.boyunfile.service.UserService;
import com.boyun.boyunfile.util.JwtUtil;
import com.boyun.boyunfile.vo.LoginVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "user", description = "该接口为用户接口，主要做用户登录，注册和校验token")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private JwtUtil jwtUtil;

    @PostMapping(value = "/register")
    @ResponseBody
    @Operation(summary = "用户注册", description = "注册账号", tags = {"user"})
    public RestResult<String> register(@RequestBody RegisterDTO registerDTO) {

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setTelephone(registerDTO.getTelephone());
        user.setPassword(registerDTO.getPassword());

        return userService.registerUser(user);

    }

    @GetMapping(value = "/login")
    @ResponseBody
    @Operation(summary = "用户登录", description = "用户登录认证后才能进入系统", tags = {"user"})
    public RestResult<LoginVO> userLogin(String telephone, String password) {

        System.out.println(telephone + " " + password);

        LoginVO loginVO = new LoginVO();
        User user = new User();
        user.setTelephone(telephone);
        user.setPassword(password);
        RestResult<User> loginResult = userService.login(user);

        if (!loginResult.getSuccess()) {
            return RestResult.fail().message(loginResult.getMessage());
        }

        loginVO.setUsername(loginResult.getData().getUsername());
        String jwt = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jwt = jwtUtil.createJWT(objectMapper.writeValueAsString(loginResult.getData()));
        } catch (Exception e) {
            return RestResult.fail().message("登录失败！");
        }
        loginVO.setToken(jwt);

        return RestResult.success().data(loginVO);
    }

    @GetMapping("/checkuserlogininfo")
    @ResponseBody
    @Operation(summary = "检查用户登录信息", description = "验证token的有效性", tags = {"user"})
    public RestResult<User> checkToken(@RequestHeader("token") String token) {

        User tokenUserInfo = null;
        try {

            Claims c = jwtUtil.parseJWT(token);
            String subject = c.getSubject();
            ObjectMapper objectMapper = new ObjectMapper();
            tokenUserInfo = objectMapper.readValue(subject, User.class);

        } catch (Exception e) {
            log.error("检查用户登录信息->解码异常");
            return RestResult.fail().message("认证失败");

        }

        if (tokenUserInfo != null) {

            return RestResult.success().data(tokenUserInfo);

        } else {
            return RestResult.fail().message("用户暂未登录");
        }
    }

//    /**
//     * 成功响应测试
//     */
//    @GetMapping(value="/test1")
//    @ResponseBody
//    public RestResult test1(){
//        return RestResult.success().message("5555");
//    }
//
//    /**
//     * 失败响应测试
//     */
//    @GetMapping(value="/test2")
//    @ResponseBody
//    public RestResult test2(){
//        return RestResult.fail();
//    }
//
//    /**
//     * 空指针异常响应测试
//     */
//    @GetMapping(value="/test3")
//    @ResponseBody
//    public RestResult test3(){
//        String s = null;
//        int i = s.length();
//        return RestResult.success();
//    }
}