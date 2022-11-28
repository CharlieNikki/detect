package com.example.detect.controller;

import com.example.detect.constant.Sign;
import com.example.detect.entity.Test;
import com.example.detect.service.TestService;
import com.example.detect.utils.Result;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.example.detect.constant.Sign.*;

@RestController
@Api(tags = "测试接口")
public class TestController {

    @Resource
    private TestService service;

    @SneakyThrows
    @PostMapping("/pic")
    public Result pic(MultipartFile[] images) {

        String url = "/files";
        String path = System.getProperty("user.dir") + url;
        Result result = new Result();

        File realPath = new File(path);
        if (!realPath.exists()) {
            realPath.mkdirs();
        }

        long count = Arrays.stream(images)
                .map(MultipartFile::getOriginalFilename)
                .filter(String::isEmpty).count();

        if (count != images.length) {
            for (MultipartFile image : images) {
                String originalFilename = image.getOriginalFilename();

                image.transferTo(new File(realPath + "/" + originalFilename));
            }
            result.setCode(RETURN_CODE_SUCCESS);
            result.setMsg(RETURN_MESSAGE_SUCCESS);
        } else {
            result.setCode(RETURN_CODE_FAIL);
            result.setMsg(RETURN_MESSAGE_FAIL);
        }
        return result;
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
