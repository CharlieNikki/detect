package com.example.detect.mapper;

import com.example.detect.entity.Image;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageMapper {

    int insertImage(Image image);

    int deleteImageById(Integer id);

    List<Image> selectImagesByProjectId(Integer projectId);

    Image selectImageById(Integer id);
}
