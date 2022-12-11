package com.example.detect.service.impl;

import com.example.detect.entity.DetectRequest;
import com.example.detect.mapper.DetectRequestMapper;
import com.example.detect.service.DetectRequestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DetectRequestServiceImpl implements DetectRequestService {

    @Resource
    private DetectRequestMapper mapper;

    @Override
    public int updateStatusToDetecting(String id) {
        return mapper.updateStatusToDetecting(id);
    }

    @Override
    public int updateStatusToComplete(String projectId) {
        return mapper.updateStatusToComplete(projectId);
    }

    @Override
    public int selectDataStatusById(String projectId) {
        return mapper.selectDataStatusById(projectId);
    }

    @Override
    public List<DetectRequest> selectDetectRequestByStatus(Integer dataStatus) {
        return mapper.selectDetectRequestByStatus(dataStatus);
    }

    @Override
    public int updateDetectStatusAndDateByProjectId(String projectId, String date) {
        return mapper.updateDetectStatusAndDateByProjectId(projectId, date);
    }
}
