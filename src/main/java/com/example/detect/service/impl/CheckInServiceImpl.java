package com.example.detect.service.impl;

import com.example.detect.entity.CheckInSheet;
import com.example.detect.mapper.CheckInSheetMapper;
import com.example.detect.service.CheckInService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CheckInServiceImpl implements CheckInService {

    @Resource
    private CheckInSheetMapper mapper;

    @Override
    public int insertSheet(CheckInSheet sheet) {
        return mapper.insertSheet(sheet);
    }

    @Override
    public CheckInSheet personExists(Integer userId) {
        return  mapper.personExists(userId);
    }

    @Override
    public int updateCheckIn(CheckInSheet sheet) {
        return mapper.updateCheckIn(sheet);
    }

    @Override
    public int updateCheckOut(CheckInSheet sheet) {
        return mapper.updateCheckOut(sheet);
    }
}
