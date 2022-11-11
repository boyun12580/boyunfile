package com.shiyanlou.file.service.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiyanlou.file.common.RestResult;
import com.shiyanlou.file.mapper.UserMapper;
import com.shiyanlou.file.model.User;
import com.shiyanlou.file.service.UserService;
import com.shiyanlou.file.util.DateUtil;
import com.shiyanlou.file.util.JWTUtil;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    UserMapper userMapper;
    @Resource
    JWTUtil jwtUtil;

    @Override
    public RestResult<String> registerUser(User user) {
        //判断验证码
        String telephone = user.getTelephone();
        String password = user.getPassword();

        if (!StringUtils.hasLength(telephone) || !StringUtils.hasLength(password)){
            return RestResult.fail().message("手机号或密码不能为空！");
        }
        if (isTelePhoneExit(telephone)){
            return RestResult.fail().message("手机号已存在！");
        }


        String salt = UUID.randomUUID().toString().replace("-", "").substring(15);
        String passwordAndSalt = password + salt;
        String newPassword = DigestUtils.md5DigestAsHex(passwordAndSalt.getBytes());

        user.setSalt(salt);

        user.setPassword(newPassword);
        user.setRegisterTime(DateUtil.getCurrentTime());
        int result = userMapper.insert(user);

        if (result == 1) {
            return RestResult.success();
        } else {
            return RestResult.fail().message("注册用户失败，请检查输入信息！");
        }
    }

    private boolean isTelePhoneExit(String telePhone) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getTelephone, telePhone);
        List<User> list = userMapper.selectList(lambdaQueryWrapper);
        if (list != null && !list.isEmpty()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public RestResult<User> login(User user) {
        String telephone = user.getTelephone();
        String password = user.getPassword();

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getTelephone, telephone);
        User saveUser = userMapper.selectOne(lambdaQueryWrapper);
        String salt = saveUser.getSalt();
        String passwordAndSalt = password + salt;
        String newPassword = DigestUtils.md5DigestAsHex(passwordAndSalt.getBytes());
        if (newPassword.equals(saveUser.getPassword())) {
            saveUser.setPassword("");
            saveUser.setSalt("");
            return RestResult.success().data(saveUser);
        } else {
            return RestResult.fail().message("手机号或密码错误！");
        }

    }

    @Override
    public User getUserByToken(String token) {
        User tokenUserInfo = null;
        try {

            Claims c = jwtUtil.parseJWT(token);
            String subject = c.getSubject();
            ObjectMapper objectMapper = new ObjectMapper();
            tokenUserInfo = objectMapper.readValue(subject, User.class);

        } catch (Exception e) {
            log.error("解码异常");
            return null;

        }
        return tokenUserInfo;
    }

}