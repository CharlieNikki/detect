package com.example.detect.mapper;

import com.example.detect.entity.DetectRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DetectRecordMapper {

    int insertDetectRecord(DetectRecord record);


    //@Select("select id, description, date, project_id, detect_person_id from detect_records where project_id = #{id}")
    List<DetectRecord> selectRecordByProjectId(Integer id);
}
