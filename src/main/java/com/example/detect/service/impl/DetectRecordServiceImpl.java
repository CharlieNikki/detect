package com.example.detect.service.impl;

import com.example.detect.entity.DetectRecord;
import com.example.detect.mapper.DetectRecordMapper;
import com.example.detect.mapper.DetectRequestMapper;
import com.example.detect.service.DetectRecordService;
import org.springframework.stereotype.Service;

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
    public List<DetectRecord> selectRecordByProjectId(Integer id) {
        return mapper.selectRecordByProjectId(id);
    }

    @Override
    public Object getImageByRecordId(Integer id) {
        return mapper.getImageByRecordId(id);
    }
}
