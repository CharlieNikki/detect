package com.example.detect.mapper;

import com.example.detect.entity.Image;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageMapper {

    int insertImage(Image image);

    int deleteImageById(String id);

    List<Image> selectImagesByProjectId(String projectId);

    Image selectImageById(String id);
}
