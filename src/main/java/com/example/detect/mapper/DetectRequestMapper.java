package com.example.detect.mapper;

import com.example.detect.entity.DetectRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DetectRequestMapper {

    @Update("update detect_request set data_status = 2 where id = #{id}")
    int updateStatusToDetecting(@Param("id") Integer projectId);

    @Update("update detect_request set data_status = 3 where id = #{id}")
    int updateStatusToComplete(@Param("id") Integer projectId);

    @Select("select data_status from detect_request where id = #{id}")
    int selectDataStatusById(@Param("id") Integer id);

    List<DetectRequest> selectDetectRequestByStatus(Integer dataStatus);

    @Update("update detect_request set detect_date = #{date} where id = #{id}")
    int updateDetectDateByProjectId(@Param("id") Integer projectId, @Param("date") String date);
}
