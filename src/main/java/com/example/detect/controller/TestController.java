package com.example.detect.controller;

import com.example.detect.entity.Test;
import com.example.detect.service.TestService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Resource
    private TestService service;

    @PostMapping("/hello")
    public String hello111(MultipartFile file) throws IOException {
        Test test = new Test();
        int i = 0;
        if (file != null) {
            BASE64Encoder encoder = new BASE64Encoder();
            String image = encoder.encode(file.getBytes());
            test.setImage(image);
            i = service.insertImage(test);
        }
        String msg = test.toString() + ":" + i;
        return test.toString();
    }

    @PostMapping(value = "/queryById", produces = "image/jpeg")
    public String queryById(HttpServletResponse response, Integer id) throws IOException {
        Test test = service.selectTestById(id);
        byte[] bytes = (byte[]) test.getImage();
        String data = new String(bytes, "UTF-8");
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

    @GetMapping("/getImage")
    public Map<String,Object> getImage(Integer id) {

        Test test = service.selectTestById(id);
        byte[] bytes = (byte[]) test.getImage();
        HashMap<String,Object> map = new HashMap<>();
        map.put("data", 1111);

        BASE64Encoder encoder = new BASE64Encoder();
        String data = encoder.encode(bytes);
        map.put("image", data);
        return map;
    }
}
