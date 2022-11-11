package com.boyun.boyunfile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyun.boyunfile.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    void insertUser(User user);
    List<User> selectUser();
}