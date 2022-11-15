package com.example.detect.controller;

import com.alibaba.fastjson2.JSON;
import com.example.detect.entity.User;
import com.example.detect.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String register(User user) {
        int i = userService.saveUserInfo(user);
        if (i != 1) {
            return "用户注册失败！";
        } else {
            return "用户注册成功！";
        }
    }

    /**
     * 登陆验证
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public String login(String username, String password) {
        User user = userService.selectUserInfo(username, password);
        if (user != null) {
            return "登陆成功！";
        } else {
            return "用户名或密码不正确";
        }
    }

    /**
     * 点击“个人资料”，返回个人资料数据
     * 根据id查看个人资料
     */
    @PostMapping("/getUserInfo")
    @ResponseBody
    public String getUserInfo(Integer id) {
        User user = userService.getUserInfoById(id);
        if (user != null) {
            return JSON.toJSONString(user);
        } else {
            return "找不到个人资料";
        }
    }
}
