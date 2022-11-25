package com.example.detect.controller;

import com.example.detect.constant.Sign;
import com.example.detect.entity.CheckInSheet;
import com.example.detect.service.CheckInService;
import com.example.detect.utils.DateUtil;
import com.example.detect.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@Api(tags = "用户签到和签退接口")
public class CheckInController {

    @Resource
    private CheckInService service;

    /**
     * 签到
     * 提供参数
     *      Integer userId
     *      BigDecimal longitude
     *      BigDecimal latitude
     * @return
     */
    @PostMapping("/checkIn")
    @ApiOperation("用户签到接口")
    @ResponseBody
    public Result checkIn(@Param("userId") Integer userId,
                          @Param("longitude") BigDecimal longitude,
                          @Param("latitude") BigDecimal latitude) {

        String date = DateUtil.dateFormat();
        CheckInSheet c = new CheckInSheet();
        Result result = new Result();

        try {
            c.setId(userId);
            c.setLongitude(longitude);
            c.setLatitude(latitude);
            c.setCheckInDate(date);

            System.out.println(c);

            CheckInSheet user = service.personExists(userId);
            if (user != null) {
                // 该员工已存在于签到表，将其date和经纬度数据进行更新
                int updateResult = service.updateCheckIn(c);
                if (updateResult == 1) {
                    result.setCode(Sign.RETURN_CODE_SUCCESS);
                    result.setMsg("打卡成功");
                    result.setData(c);
                } else {
                    result.setCode(Sign.RETURN_CODE_FAIL);
                    result.setMsg("打卡失败");
                }
            } else {
                // 该员工未存在于签到表，将其信息加入并更新date和location数据
                int insertResult = service.insertSheet(c);
                if (insertResult == 1) {
                    result.setCode(Sign.RETURN_CODE_SUCCESS);
                    result.setMsg("打卡成功");
                    result.setData(c);
                } else {
                    result.setCode(Sign.RETURN_CODE_FAIL);
                    result.setMsg("打卡失败");
                }
            }
        } catch (Exception e) {
            result.setCode(Sign.SYSTEM_CODE_ERROR);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
