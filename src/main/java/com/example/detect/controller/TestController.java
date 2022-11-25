package com.example.detect.controller;

import com.example.detect.entity.Test;
import com.example.detect.service.TestService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@Api(tags = "测试接口")
public class TestController {

    @Resource
    private TestService service;

    @PostMapping("/hello")
    public String hello111(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        StringBuffer requestURL = request.getRequestURL();
        return requestURI;
    }

    @GetMapping(value = "/queryById", produces = "image/jpeg")
    public String queryById(HttpServletResponse response, Integer id) throws IOException {
        Test test = service.selectTestById(id);
        byte[] bytes = (byte[]) test.getImage();
        String data = new String(bytes, StandardCharsets.UTF_8);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(data);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        response.setContentType("image/jpeg");
        ServletOutputStream out = response.getOutputStream();
        out.write(b);
        out.flush();
        out.close();

        return test.toString();
    }
}
