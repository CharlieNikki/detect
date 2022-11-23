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

    @Override
    public int saveUserInfo(User user) {
        return userMapper.saveInfo(user);
    }

    @Override
    public User selectUserInfo(String phone, String password) {
        return userMapper.getInfo(phone, password);
    }

    @Override
    public User getUserInfoById(Integer id) {
        return userMapper.getInfoById(id);
    }

    @Override
    public int updateUserInfo(User user) {
        return userMapper.updateUserInfo(user);
    }
}
