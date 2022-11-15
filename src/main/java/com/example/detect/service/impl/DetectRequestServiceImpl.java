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
    public int updateStatusToDetecting(Integer id) {
        return mapper.updateStatusToDetecting(id);
    }

    @Override
    public int updateStatusToComplete(Integer id) {
        return mapper.updateStatusToComplete(id);
    }

    @Override
    public int selectDataStatusById(Integer id) {
        return mapper.selectDataStatusById(id);
    }

    @Override
    public List<DetectRequest> selectDetectRequestByStatus(Integer dataStatus) {
        return mapper.selectDetectRequestByStatus(dataStatus);
    }

    @Override
    public int updateDetectDateByProjectId(Integer projectId, String date) {
        return mapper.updateDetectDateByProjectId(projectId, date);
    }


}
