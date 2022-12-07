package com.example.detect.service;

import com.example.detect.entity.Image;

import java.util.List;

public interface ImageService {

    int insertImage(Image image);

    boolean deleteImageById(Integer id);

    List<Image> selectImagesByProjectId(Integer projectId);
}
