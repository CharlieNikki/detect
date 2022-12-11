package com.example.detect.controller;

import com.example.detect.entity.User;
import com.example.detect.service.UserService;
import com.example.detect.utils.Result;
import com.example.detect.utils.SnowflakeIdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.example.detect.constant.Sign.*;

@RestController
@Api(tags = "用户管理相关接口")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @ApiOperation("用户注册接口")
    @ResponseBody
    @PostMapping("/register")
    public Result register(User user) {

        Result result = new Result();
        // 使用雪花算法为用户生成唯一id
        user.setUserId(String.valueOf(new SnowflakeIdWorker(0,0).nextId()));
        // 注册成功标识
        boolean isRegister = false;
        try {
            // 用户注册
            isRegister = userService.saveUserInfo(user);
        } catch (Exception e) {
            result.setResult(SYSTEM_CODE_ERROR, e.getMessage(), null, 0);
        }
        if (isRegister) {
            // 注册成功
            result.setResult(RETURN_CODE_SUCCESS, RETURN_MESSAGE_SUCCESS, user, 1);
        } else {
            // 注册失败
            result.setResult(RETURN_CODE_FAIL, "该手机号已经存在", null, 0);
        }
        return result;
    }

    /**
     * 登陆验证
     */
    @PostMapping("/login")
    @ResponseBody
    @ApiOperation("用户登录接口")
    public Result login(@RequestParam("phone") String phone,
                        @RequestParam("password") String password) {

        Result result = new Result();
        User user = null;
        try {
            // 判断该用户是否存在
            user = userService.selectUserInfo(phone);
        } catch (Exception e) {
            result.setResult(SYSTEM_CODE_ERROR, e.getMessage(), null, 0);
        }
        // 该用户存在
        if (user != null) {
            // 判断密码是否匹配
            if (user.getPassword().equals(password)) {
                // 密码匹配，登陆成功
                result.setResult(RETURN_CODE_SUCCESS, RETURN_MESSAGE_SUCCESS, user, 1);
            } else {
                // 密码不匹配，登陆失败
                result.setResult(RETURN_CODE_FAIL, "密码错误", null, 0);
            }
        } else {
            // 该用户不存在
            result.setResult(RETURN_CODE_FAIL, "该用户不存在", null, 0);
        }
        return result;
    }

    /**
     * 查看个人资料
     */
    @GetMapping("/getUserInfo")
    @ResponseBody
    @ApiOperation("查看个人资料接口")
    public Result getUserInfo(@RequestParam("id") String id) {

        Result result = new Result();
        User user = null;
        try {
            // 根据用户id查看个人资料
            user = userService.getUserInfoById(id);
        } catch (Exception e) {
            result.setResult(SYSTEM_CODE_ERROR, e.getMessage(), null, 0);
        }
        if (user != null) {
            result.setResult(RETURN_CODE_SUCCESS, RETURN_MESSAGE_SUCCESS, user, 1);
        } else {
            result.setResult(RETURN_CODE_FAIL, "无法查看个人资料", null, 0);
        }
        return result;
    }

    /**
     * 更改用户信息
     */
    @ApiOperation("更改用户信息")
    @PostMapping("/updateInfo")
    @ResponseBody
    public Result updateInfo(User user) {

        Result result = new Result();
        boolean isUpdate = false;
        try {
            int i = userService.updateUserInfo(user);
            if (i == 1) {
                // 更改成功
                result.setCode(RETURN_CODE_SUCCESS);
                result.setMsg(RETURN_MESSAGE_SUCCESS);
                result.setData(user);
            } else {
                result.setCode(RETURN_CODE_FAIL);
                result.setMsg(RETURN_MESSAGE_FAIL);
            }
        } catch (Exception e) {
            result.setCode(SYSTEM_CODE_ERROR);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
