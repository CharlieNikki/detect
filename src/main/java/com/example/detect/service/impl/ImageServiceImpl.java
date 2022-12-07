package com.example.detect.service.impl;

import com.example.detect.entity.Image;
import com.example.detect.mapper.ImageMapper;
import com.example.detect.service.ImageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Resource
    private ImageMapper mapper;

    @Override
    public List<Image> selectImagesByProjectId(Integer projectId) {
        return mapper.selectImagesByProjectId(projectId);
    }

    @Override
    public boolean deleteImageById(Integer id) {
        boolean flag = false;
        Image image = mapper.selectImageById(id);
        String imageName = image.getImageName();
        // 拼接成路径地址
        // Windows
        //String imagePath = "E:/images/" + imageName;
        // Linux
        String imagePath = "/usr/local/detect/images/" + imageName;
        // 根据路径创建文件对象
        File file = new File(imagePath);
        // 路径是个文件且文件不为空时删除文件
        if (file.isFile() && file.exists()) {
            // 从数据库中删除对应的图片信息
            int deleteResult = mapper.deleteImageById(id);
            if (deleteResult == 1) {
                // 数据库删除成功之后，删除本地对应的图片信息
                flag = file.delete();
            }
        }
        return flag;
    }

    @Override
    public int insertImage(Image image) {
        return mapper.insertImage(image);
    }
}
