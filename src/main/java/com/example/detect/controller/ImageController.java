package com.example.detect.controller;

import com.example.detect.entity.Image;
import com.example.detect.service.ImageService;
import com.example.detect.utils.DateUtil;
import com.example.detect.utils.FileUtil;
import com.example.detect.utils.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.util.List;

import static com.example.detect.constant.Sign.*;

@RestController
public class ImageController {

    @Resource
    private ImageService service;

    /**
     * 新增图片
     */
    @PostMapping("/insertImage")
    public Result insertImage(@RequestPart(value = "file")MultipartFile file,
                              @RequestParam(value = "projectId") String projectId) {

        Result result = new Result();
        boolean isInsert = false;

        try {
            isInsert = service.insertImage(file, projectId);
        } catch (Exception e) {
            result.setResult(SYSTEM_CODE_ERROR, e.getMessage(), null, 0);
            e.printStackTrace();
        }
        if (isInsert) {
            result.setResult(RETURN_CODE_SUCCESS, RETURN_MESSAGE_SUCCESS, null, 1);
        } else {
            result.setResult(RETURN_CODE_FAIL, "图片新增失败", null, 0);
        }
        return result;
    }

    /**
     * 根据图片id删除图片
     * @param id 图片id
     * @return
     */
    @PostMapping("/deleteImage")
    public Result deleteImage(@RequestParam(value = "id") String id) {

        boolean deleteResult = false;
        Result result = new Result();
        try {
            // 根据图片id删除图片信息
            deleteResult = service.deleteImageById(id);
        } catch (Exception e) {
            result.setResult(SYSTEM_CODE_ERROR, e.getMessage(), null, 0);
            e.printStackTrace();
        }
        if (deleteResult) {
            result.setResult(RETURN_CODE_SUCCESS, RETURN_MESSAGE_SUCCESS, null, 1);
        } else {
            result.setResult(RETURN_CODE_FAIL, "没有这张图片", null, 0);
        }
        return result;
    }

    /**
     * 根据projectId获取图片信息
     */
    @GetMapping("/getImages")
    @ResponseBody
    public Result getImages(String projectId) {

        Result result = new Result();
        List<Image> images = service.selectImagesByProjectId(projectId);
        result.setResult(RETURN_CODE_SUCCESS, RETURN_MESSAGE_SUCCESS, images, images.size());

        return result;
    }
}
