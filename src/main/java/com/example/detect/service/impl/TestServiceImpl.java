package com.example.detect.service.impl;

import com.example.detect.entity.Test;
import com.example.detect.mapper.TestMapper;
import com.example.detect.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TestServiceImpl implements TestService {

    @Resource
    private TestMapper mapper;

    @Override
    public int insertImage(Test test) {
        return mapper.insertImage(test);
    }

    @Override
    public Test selectTestById(Integer id) {
        return mapper.selectTestById(id);
    }
}
