package com.example.detect.service;

import com.example.detect.entity.Test;

public interface TestService {

    int insertImage(Test test);

    Test selectTestById(Integer id);
}
