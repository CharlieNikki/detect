package com.example.detect.service.impl;

import com.example.detect.entity.User;
import com.example.detect.mapper.UserMapper;
import com.example.detect.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册service
     */
    @Override
    public boolean saveUserInfo(User user) {
        return userMapper.saveInfo(user) == 1;
    }

    /**
     * 用户登录验证service
     */
    @Override
    public User selectUserInfo(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public User getUserInfoById(String id) {
        return userMapper.getInfoById(id);
    }

    @Override
    public int updateUserInfo(User user) {
        return userMapper.updateUserInfo(user);
    }
}
