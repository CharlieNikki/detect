package com.example.detect.service;

import com.example.detect.entity.User;

public interface UserService {

    boolean saveUserInfo(User user);

    User selectUserInfo(String phone, String password);

    User getUserInfoById(Integer id);

    int updateUserInfo(User user);
}
