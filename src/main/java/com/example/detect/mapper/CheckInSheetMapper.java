package com.example.detect.mapper;

import com.example.detect.entity.CheckInSheet;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CheckInSheetMapper {

    @Insert("insert into check_in_sheet(user_id, date, location) values(#{userId}, #{date}, #{location})")
    int insertSheet(CheckInSheet sheet);

    @Select("select * from check_in_sheet where user_id = #{userId}")
    CheckInSheet personExists(@Param("userId") Integer userId);

    @Update("update check_in_sheet set date = #{date}, location = #{location} where user_id = #{userId}")
    int updateCheckIn(CheckInSheet sheet);
}
