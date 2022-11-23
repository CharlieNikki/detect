package com.example.detect.controller;

import com.alibaba.fastjson2.JSON;
import com.example.detect.entity.User;
import com.example.detect.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "用户管理相关接口")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册
     *      username
     *      password
     *      companyName
     *      phone
     * @return
     */
    @ApiOperation("用户注册接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名"),
            @ApiImplicitParam(name = "password", value = "密码"),
            @ApiImplicitParam(name = "companyName", value = "公司名称"),
            @ApiImplicitParam(name = "phone", value = "手机号")
    })
    @PostMapping("/register")
    public String register(String username, String password, String companyName, String phone) {

        User user = new User();
        user.setUsername(username);
        user.setUsername(password);
        user.setUsername(companyName);
        user.setUsername(phone);

        int i = userService.saveUserInfo(user);
        if (i != 1) {
            return "用户注册失败！";
        } else {
            return "用户注册成功！";
        }
    }

    /**
     * 登陆验证
     * @param phone
     * @param password
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    @ApiOperation("用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号"),
            @ApiImplicitParam(name = "password", value = "密码")
    })
    public String login(@Param("phone")String phone, @Param("password")String password) {
        User user = userService.selectUserInfo(phone, password);
        if (user != null) {
            return JSON.toJSONString(user);
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
    @ApiOperation("查看个人资料接口")
    @ApiImplicitParam(name = "id", value = "用户id")
    public String getUserInfo(@Param("id") Integer id) {
        User user = userService.getUserInfoById(id);
        if (user != null) {
            return JSON.toJSONString(user);
        } else {
            return "找不到个人资料";
        }
    }

    /**
     * 需要参数
     *      userId
     *      username
     *      companyName
     *      phone
     * @param user
     * @return
     */
    @ApiOperation("更改用户信息")
    @PostMapping("/updateInfo")
    public String updateInfo(User user) {

        int i = userService.updateUserInfo(user);
        if (i == 1) {
            // 更改成功
            return "更改成功";
        } else {
            return "更改失败";
        }
    }
}
