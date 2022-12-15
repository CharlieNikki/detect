package com.example.detect.mapper;

import com.example.detect.entity.AttendanceInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DetectAttendanceMapper {

    int insertInfo(AttendanceInfo info);

    AttendanceInfo selectInfoByUserId(String userId);

    int updateDateByUserId(AttendanceInfo info);

}
