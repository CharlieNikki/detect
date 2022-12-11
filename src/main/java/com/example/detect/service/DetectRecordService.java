package com.example.detect.service;

import com.example.detect.entity.DetectRecord;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface DetectRecordService {

    boolean addDetectRecord(DetectRecord record);

    DetectRecord selectRecordByProjectId(String projectId);

    Object getImageByRecordId(String id);

    boolean updateDetectRecord(DetectRecord record);

    int addDetectImage(String image, String projectId);

    boolean completeDetect(String projectId);

    Map<String,Object> getRecordByProjectId(String projectId);
}
