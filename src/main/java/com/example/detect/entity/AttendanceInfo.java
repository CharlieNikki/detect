package com.example.detect.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttendanceInfo {

    @ApiModelProperty(value = "唯一标识")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "最近一次签到日期")
    private String date;

    @ApiModelProperty("打卡经度")
    private BigDecimal longitude;

    @ApiModelProperty("打卡纬度")
    private BigDecimal latitude;

    @ApiModelProperty("签到位置")
    private String location;
}
