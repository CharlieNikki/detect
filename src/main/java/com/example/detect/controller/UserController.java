package com.example.detect.controller;

import com.example.detect.entity.User;
import com.example.detect.service.UserService;
import com.example.detect.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @ResponseBody
    @PostMapping("/register")
    public Result register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("companyName") String companyName,
                           @RequestParam("phone") String phone) {

        Result result = new Result();
        try {
            User user = new User();

            String name = username.trim();
            String pwd = password.trim();
            String cname = companyName.trim();
            String pNum = phone.trim();

            user.setUsername(name);
            user.setPassword(pwd);
            user.setCompanyName(cname);
            user.setPhone(pNum);
            System.out.println(user);

            int i = userService.saveUserInfo(user);
            if (i != 1) {
                result.setCode(RETURN_CODE_FAIL);
                result.setMsg("该手机号已存在");
            } else {
                result.setCode(RETURN_CODE_SUCCESS);
                result.setMsg(RETURN_MESSAGE_SUCCESS);
            }
        } catch (Exception e) {
            result.setCode(SYSTEM_CODE_ERROR);
            result.setMsg(e.getMessage());
        }
        return result;
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
    public Result login(@RequestParam("phone") String phone,
                        @RequestParam("password") String password) {
        Result result = new Result();
        try {
            User user = userService.selectUserInfo(phone, password);
            if (user == null) {
                result.setCode(RETURN_CODE_FAIL);
                result.setMsg("用户不存在或密码错误！");
            } else {
                result.setCode(RETURN_CODE_SUCCESS);
                result.setMsg(RETURN_MESSAGE_SUCCESS);
                result.setData(user);
            }
        } catch (Exception e) {
            result.setCode(SYSTEM_CODE_ERROR);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 点击“个人资料”，返回个人资料数据
     * 根据id查看个人资料
     */
    @GetMapping("/getUserInfo")
    @ResponseBody
    @ApiOperation("查看个人资料接口")
    @ApiImplicitParam(name = "id", value = "用户id")
    public Result getUserInfo(@RequestParam("id") Integer id) {

        Result result = new Result();
        try {
            User user = userService.getUserInfoById(id);
            if (user != null) {
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

    /**
     * 需要参数
     *      userId
     *      username
     *      companyName
     *      password
     * @param
     * @return
     */
    @ApiOperation("更改用户信息")
    @PostMapping("/updateInfo")
    @ResponseBody
    public Result updateInfo(@RequestParam("username") String username,
                             @RequestParam("companyName") String companyName,
                             @RequestParam("password") String password,
                             @RequestParam("phone") String phone) {

        Result result = new Result();
        User user = new User();

        try {
            String name = username.trim();
            String cname = companyName.trim();
            String pwd = password.trim();
            String ph = phone.trim();

            user.setUsername(name);
            user.setPassword(pwd);
            user.setCompanyName(cname);
            user.setPhone(ph);
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
