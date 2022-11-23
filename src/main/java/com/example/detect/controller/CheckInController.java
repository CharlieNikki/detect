package com.example.detect.controller;

import com.example.detect.entity.CheckInSheet;
import com.example.detect.service.CheckInService;
import com.example.detect.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "用户签到和签退接口")
public class CheckInController {

    @Resource
    private CheckInService service;

    /**
     * 签到
     * 提供参数
     *      userId
     *      checkInLocation
     * @param c
     * @return
     */

    @PostMapping("/checkIn")
    @ApiOperation("用户签到接口")
    public String checkIn(CheckInSheet c) {
        String date = DateUtil.dateFormat();
        c.setCheckInDate(date);
        CheckInSheet user = service.personExists(c.getUserId());
        if (user != null) {
            // 该员工已存在于签到表，将其date和location数据进行更新
            int updateResult = service.updateCheckIn(c);
            if (updateResult == 1)
                return "签到完成！";
            else
                return "签到失败！";

        } else {
            // 该员工未存在于签到表，将其信息加入并更新date和location数据
            int insertResult = service.insertSheet(c);
            if (insertResult == 1)
                return "签到完成！";
            else
                return "签到失败！";
        }
    }

    /**
     * 签退
     *      接收参数：
     *          checkOutLocation
     *          userId
     */
    @ApiOperation("用户签退接口")
    @PostMapping("checkOut")
    public String checkOut(CheckInSheet sheet) {
        String date = DateUtil.dateFormat();
        sheet.setCheckOutDate(date);
        int i = service.updateCheckOut(sheet);
        if (i == 1) return "签退成功！";
        return "签退失败!";
    }
}
