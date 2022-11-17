package com.example.detect.mapper;

import com.example.detect.entity.CheckInSheet;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CheckInSheetMapper {

    @Insert("insert into check_in_sheet(user_id, check_in_date, check_in_location) values(#{userId}, #{checkInDate}, #{checkInLocation})")
    int insertSheet(CheckInSheet sheet);

    @Select("select * from check_in_sheet where user_id = #{userId}")
    CheckInSheet personExists(@Param("userId") Integer userId);

    @Update("update check_in_sheet set check_in_date = #{checkInDate}, check_in_location = #{checkInLocation} where user_id = #{userId}")
    int updateCheckIn(CheckInSheet sheet);

    @Update("update check_in_sheet set check_out_date = #{checkOutDate}, check_out_location = #{checkOutLocation} where user_id = #{userId}")
    int updateCheckOut(CheckInSheet sheet);
}
