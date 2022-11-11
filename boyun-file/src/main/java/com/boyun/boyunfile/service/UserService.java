package com.boyun.boyunfile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyun.boyunfile.common.RestResult;
import com.boyun.boyunfile.domain.User;


public interface UserService extends IService<User> {
    RestResult<String> registerUser(User user);
    RestResult<User> login(User user);
    User getUserByToken(String token);
}