package com.example.detect.service.impl;

import com.example.detect.entity.AttendanceInfo;
import com.example.detect.mapper.DetectAttendanceMapper;
import com.example.detect.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private DetectAttendanceMapper mapper;

    /**
     * 外检人员考勤service
     */
    @Override
    public boolean detectAttendance(AttendanceInfo info) {

        boolean flag;
        // 判断userId是否已存于签到表内
        if (mapper.selectInfoByUserId(info.getUserId()) != null) {
            // 存在，则进行更新(日期、地点经纬度)
            flag = mapper.updateDateByUserId(info) == 1;
        } else {
            // 若不存在，则进行add操作
            flag = mapper.insertInfo(info) == 1;
        }
        return flag;
    }
}
