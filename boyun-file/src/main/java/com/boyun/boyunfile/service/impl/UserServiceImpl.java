package com.boyun.boyunfile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyun.boyunfile.common.RestResult;
import com.boyun.boyunfile.domain.User;
import com.boyun.boyunfile.mapper.UserMapper;
import com.boyun.boyunfile.service.UserService;
import com.boyun.boyunfile.util.DateUtil;
import com.boyun.boyunfile.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public RestResult<String> registerUser(User user) {

        String telephone = user.getTelephone();
        String password = user.getPassword();

        if(!StringUtils.hasLength(telephone) || !StringUtils.hasLength(password)){
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
            return RestResult.success().message("注册成功！");
        } else {
            return RestResult.fail().message("注册用户失败，请检查输入信息！");
        }

    }

    @Override
    public RestResult<User> login(User user) {
        String telephone = user.getTelephone();
        String password = user.getPassword();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getTelephone, telephone);
        User saveUser = userMapper.selectOne(queryWrapper);
        if(saveUser == null){
            return RestResult.fail().message("用户不存在");
        }
        String salt = saveUser.getSalt();
        String passwordAndSalt = password + salt;
        String newPassword = DigestUtils.md5DigestAsHex(passwordAndSalt.getBytes());
        if (newPassword.equals(saveUser.getPassword())) {
            saveUser.setPassword("");
            saveUser.setSalt("");
            return RestResult.success().data(saveUser).message("登录成功！");
        } else {
            return RestResult.fail().message("手机号或密码错误！");
        }
    }

//    通过 token 获取到用户信息，校验token
    @Override
    public User getUserByToken(String token) {
        User tokenUserInfo = null;
        try {
            Claims claims = jwtUtil.parseJWT(token);
            String subject = claims.getSubject();
            ObjectMapper mapper = new ObjectMapper();
            tokenUserInfo = mapper.readValue(subject, User.class);
        } catch (Exception e) {
            log.error("解码异常");
            return null;
        }
        return tokenUserInfo;
    }

    private boolean isTelePhoneExit(String telePhone){

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getTelephone, telePhone);
        List<User> users = userMapper.selectList(queryWrapper);
        return users != null && !users.isEmpty();
    }
}
