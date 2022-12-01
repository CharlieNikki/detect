package com.example.detect.service.impl;

import com.example.detect.entity.DetectRecord;
import com.example.detect.mapper.DetectRecordMapper;
import com.example.detect.mapper.DetectRequestMapper;
import com.example.detect.service.DetectRecordService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DetectRecordServiceImpl implements DetectRecordService {

    @Resource
    private DetectRecordMapper mapper;


    @Override
    public int addDetectRecord(DetectRecord record) {
        return mapper.insertDetectRecord(record);
    }

    @Override
    public DetectRecord selectRecordByProjectId(Integer projectId) {
        return mapper.selectRecordByProjectId(projectId);
    }

    @Override
    public Object getImageByRecordId(Integer id) {
        return mapper.getImageByRecordId(id);
    }

    @Override
    public int updateDetectRecord(DetectRecord record) {
        return mapper.updateDetectRecord(record);
    }

    @Override
    public int addDetectImage(String image, Integer projectId) {
        return mapper.addDetectImage(image, projectId);
    }
}
