package com.shiyanlou.file.service;

import com.shiyanlou.file.common.RestResult;
import com.shiyanlou.file.model.User;

public interface UserService {
    RestResult<String> registerUser(User user);
    RestResult<User> login(User user);
    User getUserByToken(String token);
}