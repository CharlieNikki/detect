package com.example.detect.mapper;

import com.example.detect.entity.DetectRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DetectRecordMapper {

    /**
     * 添加检测记录
     */
    int insertDetectRecord(DetectRecord record);

    DetectRecord selectRecordByProjectId(String projectId);

    Object getImageByRecordId(String id);

    int updateDetectRecord(DetectRecord record);

    int addDetectImage(String image, String projectId);
}
