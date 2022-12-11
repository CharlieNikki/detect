package com.example.detect.service;

import com.example.detect.entity.DetectRequest;

import java.util.List;

public interface DetectRequestService {

    int updateStatusToDetecting(String id);

    int updateStatusToComplete(String projectId);

    int selectDataStatusById(String projectId);

    List<DetectRequest> selectDetectRequestByStatus(Integer dataStatus);

    int updateDetectStatusAndDateByProjectId(String projectId, String date);
}
