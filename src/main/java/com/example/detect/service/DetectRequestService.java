package com.example.detect.service;

import com.example.detect.entity.DetectRequest;

import java.util.List;

public interface DetectRequestService {

    int updateStatusToDetecting(Integer id);

    int updateStatusToComplete(Integer projectId);

    int selectDataStatusById(Integer projectId);

    List<DetectRequest> selectDetectRequestByStatus(Integer dataStatus);

    int updateDetectDateByProjectId(Integer projectId, String date);
}
