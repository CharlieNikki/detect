package com.example.detect.service;

import com.example.detect.entity.DetectRecord;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DetectRecordService {

    int addDetectRecord(DetectRecord record);

    DetectRecord selectRecordByProjectId(Integer projectId);

    Object getImageByRecordId(Integer id);

    int updateDetectRecord(DetectRecord record);
}
