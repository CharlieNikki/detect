package com.example.detect.service;

import com.example.detect.entity.User;

public interface UserService {

    int saveUserInfo(User user);

    User selectUserInfo(String username, String password);

    User getUserInfoById(Integer id);
}
