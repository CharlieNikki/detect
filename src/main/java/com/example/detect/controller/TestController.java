package com.example.detect.controller;

import com.example.detect.constant.Sign;
import com.example.detect.entity.Test;
import com.example.detect.service.TestService;
import com.example.detect.utils.Result;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.example.detect.constant.Sign.*;

@RestController
@Api(tags = "测试接口")
public class TestController {

}
