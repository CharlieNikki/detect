package com.example.detect.service;

import com.example.detect.entity.DetectRecord;

import java.util.List;

public interface DetectRecordService {

    int addDetectRecord(DetectRecord record);

    List<DetectRecord> selectRecordByProjectId(Integer projectId);

    Object getImageByRecordId(Integer id);
}
