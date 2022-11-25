package com.example.detect.mapper;

import com.example.detect.entity.DetectRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DetectRecordMapper {

    int insertDetectRecord(DetectRecord record);

    List<DetectRecord> selectRecordByProjectId(Integer projectId);

    Object getImageByRecordId(Integer id);
}
