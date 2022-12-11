package com.example.detect.service.impl;

import com.example.detect.entity.Image;
import com.example.detect.mapper.ImageMapper;
import com.example.detect.service.ImageService;
import com.example.detect.utils.DateUtil;
import com.example.detect.utils.FileUtil;
import com.example.detect.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

import static com.example.detect.constant.Sign.RETURN_MESSAGE_FAIL;

@Service
public class ImageServiceImpl implements ImageService {

    @Resource
    private ImageMapper mapper;

    @Override
    public List<Image> selectImagesByProjectId(String projectId) {
        return mapper.selectImagesByProjectId(projectId);
    }

    /**
     * 根据图片id删除图片信息
     */
    @Override
    public boolean deleteImageById(String id) {
        boolean flag = false;
        Image image = mapper.selectImageById(id);
        String imageName = image.getImageName();
        // 拼接成路径地址
        // Windows
        //String imagePath = "D:/images/" + imageName;
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

    /**
     * 新增图片
     */
    @Override
    @Transactional
    public boolean insertImage(MultipartFile file, String projectId) {

        // 将图片数据存入本地
        String newFileName = FileUtil.fileDownload(file);

        if (!newFileName.equals(RETURN_MESSAGE_FAIL)) {
            // 图片存入本地成功。根据projectId，将图片信息存入数据库
            Image image = new Image();
            image.setImage(String.valueOf(new SnowflakeIdWorker(0,0).nextId()), projectId,
                    newFileName, DateUtil.dateFormat());
            // 判断存入数据库是否成功
            return mapper.insertImage(image) == 1;
        }
        return false;
    }
}
