package com.example.detect.constant;

public class Sign {

    /**
     * 是否请求成功的标识
     */

    // 请求成功
    public static final Integer RETURN_CODE_SUCCESS = 200;
    // 请求失败
    public static final Integer RETURN_CODE_FAIL = 404;
    // 无效请求
    public static final Integer RETURN_CODE_INVALID_REQUEST = 412;
    // 系统错误
    public static final Integer SYSTEM_CODE_ERROR = -1;

    public static final String RETURN_MESSAGE_FAIL = "请求失败";

    public static final String RETURN_MESSAGE_SUCCESS = "请求成功";

    public static final String RETURN_MESSAGE_INVALID_REQUEST = "无效的请求";

}
