package com.example.detect.service;

import com.example.detect.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    /**
     * 新增图片
     */
    boolean insertImage(MultipartFile file, String projectId);

    boolean deleteImageById(String id);

    List<Image> selectImagesByProjectId(String projectId);
}
