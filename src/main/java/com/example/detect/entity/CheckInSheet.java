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
public class CheckInSheet {

    @ApiModelProperty(value = "签到编号id")
    private Integer id;
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @ApiModelProperty(value = "最近一次签到日期")
    private String checkInDate;
    @ApiModelProperty("打卡经度")
    private BigDecimal longitude;
    @ApiModelProperty("打卡纬度")
    private BigDecimal latitude;
}
