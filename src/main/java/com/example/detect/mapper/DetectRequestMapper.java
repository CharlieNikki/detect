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
    int updateStatusToDetecting(@Param("id") String projectId);

    /**
     * 根据projectId,进行完成检测操作
     */
    @Update("update detect_request set data_status = 2 where id = #{projectId}")
    int updateStatusToComplete(String projectId);

    @Select("select data_status from detect_request where id = #{projectId}")
    int selectDataStatusById(String projectId);

    /**
     * 根据status，返回project信息
     */
    List<DetectRequest> selectDetectRequestByStatus(Integer dataStatus);

    @Update("update detect_request set detect_date = #{date}, data_status = 1 where id = #{projectId}")
    int updateDetectStatusAndDateByProjectId(String projectId, String date);

    /**
     * 根据projectId，更新detect_request #{检测时间}和 #{数据状态}
     */
    int updateStatusAndDateByProjectId(String projectId, String date, Integer status);
}
