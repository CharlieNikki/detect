package com.example.detect.mapper;

import com.example.detect.entity.DetectRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DetectRequestMapper {

    @Update("update detect_request set data_status = 1 where id = #{id}")
    int updateStatusToDetecting(@Param("id") Integer projectId);

    @Update("update detect_request set data_status = 2 where id = #{projectId}")
    int updateStatusToComplete(Integer projectId);

    @Select("select data_status from detect_request where id = #{projectId}")
    int selectDataStatusById(Integer projectId);

    List<DetectRequest> selectDetectRequestByStatus(Integer dataStatus);

    @Update("update detect_request set detect_date = #{date} where id = #{id}")
    int updateDetectDateByProjectId(@Param("id") Integer projectId, @Param("date") String date);
}
