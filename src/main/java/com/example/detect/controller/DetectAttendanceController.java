package com.example.detect.controller;

import com.example.detect.entity.AttendanceInfo;
import com.example.detect.service.AttendanceService;
import com.example.detect.utils.DateUtil;
import com.example.detect.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.detect.constant.Sign.*;

@RestController
public class DetectAttendanceController {

    @Autowired
    private AttendanceService service;

    /**
     * 签到考勤
     * @param info
     * @return
     */
    @PostMapping("/check")
    @ResponseBody
    public Result check(AttendanceInfo info) {

        Result result = new Result();
        info.setDate(DateUtil.dateFormat());
        boolean flag = false;
        System.out.println(info);
        try {
            flag = service.detectAttendance(info);
        } catch (Exception e) {
            result.setResult(SYSTEM_CODE_ERROR, e.getMessage(), null, 0);
            e.printStackTrace();
        }
        if (flag) {
            result.setResult(RETURN_CODE_SUCCESS, RETURN_MESSAGE_SUCCESS, null, 1);
        } else {
            result.setResult(RETURN_CODE_FAIL, "更新数据库失败", null, 0);
        }
        return result;
    }
}
