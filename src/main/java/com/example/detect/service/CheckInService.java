package com.example.detect.service;

import com.example.detect.entity.CheckInSheet;

public interface CheckInService {

    int insertSheet(CheckInSheet sheet);

    CheckInSheet personExists(Integer userId);

    int updateCheckIn(CheckInSheet sheet);

    int updateCheckOut(CheckInSheet sheet);
}
