package com.example.detect.service;

import com.example.detect.entity.AttendanceInfo;

public interface AttendanceService {

    /**
     * 考勤service
     */
    boolean detectAttendance(AttendanceInfo info);
}
