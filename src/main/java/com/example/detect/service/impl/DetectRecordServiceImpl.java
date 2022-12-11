package com.example.detect.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.detect.entity.DetectRecord;
import com.example.detect.entity.Image;
import com.example.detect.entity.ReturnImage;
import com.example.detect.mapper.DetectRecordMapper;
import com.example.detect.mapper.DetectRequestMapper;
import com.example.detect.mapper.ImageMapper;
import com.example.detect.service.DetectRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.detect.utils.ImageUtil.SAVE_IMAGE_RELATIVE_PATH;

@Service
public class DetectRecordServiceImpl implements DetectRecordService {

    @Resource
    private DetectRecordMapper mapper;

    @Resource
    private DetectRequestMapper requestMapper;

    @Resource
    private ImageMapper imageMapper;

    /**
     * 添加检测记录service
     */
    @Override
    @Transactional
    public boolean addDetectRecord(DetectRecord record) {
        // 向数据库中添加检测记录成功
        if (mapper.insertDetectRecord(record) == 1) {
            // 根据projectId,更新request的最新检测日期，更改request的检测状态为检测中(待受理，已受理，检测中，检测完毕)
            int updateResult = requestMapper.
                    updateStatusAndDateByProjectId(record.getProjectId(), record.getDate(), 2);
            // 若成功更改检测日期和数据状态，返回true
            return updateResult == 1;
        }
        return false;
    }

    @Override
    public DetectRecord selectRecordByProjectId(String projectId) {
        return mapper.selectRecordByProjectId(projectId);
    }

    @Override
    public Object getImageByRecordId(String id) {
        return mapper.getImageByRecordId(id);
    }

    /**
     * 根据projectId获取对应的检测信息(包括所有的图片路径)
     */
    @Override
    public Map<String, Object> getRecordByProjectId(String projectId) {

        Map<String,Object> record = new HashMap<>();
        // 根据projectId从数据库获取相应的文字信息
        DetectRecord recordInfo = mapper.selectRecordByProjectId(projectId);
        // 判断recordInfo是否为空
        if (recordInfo != null) {
            // 若recordInfo不为空，则直接存入map中，无需关注images是否为空
            record.put("recordInfo", recordInfo);
            // 根据projectId从数据库获取相应图片名称
            List<Image> imageInfo = imageMapper.selectImagesByProjectId(projectId);
            // 判断images是否为空
            if (imageInfo.size() != 0) {
                // 循环将数据赋值
                List<ReturnImage> imagesUrl = new ArrayList<>();
                for (Image image : imageInfo) {
                    ReturnImage returnImage = new ReturnImage();
                    returnImage.setId(image.getId());
                    //String imageUrl = "http://localhost:8083" + SAVE_IMAGE_RELATIVE_PATH + image.getImageName();
                    String imageUrl = "http://8.136.84.248:8083" + SAVE_IMAGE_RELATIVE_PATH + image.getImageName();
                    returnImage.setImageUrl(imageUrl);
                    imagesUrl.add(returnImage);
                }
                record.put("imageInfo", imagesUrl);
            }
        }
        // 存入Map集合中进行返回
        return record;
    }

    /**
     * 完成检测service
     */
    @Override
    public boolean completeDetect(String projectId) {
        // 更改project的检测状态
        return requestMapper.updateStatusToComplete(projectId) == 1;
    }

    /**
     * 更新检测记录信息
     */
    @Override
    public boolean updateDetectRecord(DetectRecord record) {

        return mapper.updateDetectRecord(record) == 1;
    }

    @Override
    public int addDetectImage(String image, String projectId) {
        return mapper.addDetectImage(image, projectId);
    }
}
