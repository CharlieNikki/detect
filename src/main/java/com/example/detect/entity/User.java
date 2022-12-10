package com.example.detect.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("用户密码")
    private String password;
    @ApiModelProperty("工程单位名称")
    private String companyName;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("用户id")
    private String userId;
}
