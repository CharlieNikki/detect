package com.example.detect.mapper;

import com.example.detect.entity.CheckInSheet;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CheckInSheetMapper {

    int insertSheet(CheckInSheet sheet);

    CheckInSheet personExists(@Param("userId") String userId);

    int updateCheckIn(CheckInSheet sheet);

}
