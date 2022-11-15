package com.example.detect.mapper;

import com.example.detect.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username} and password = #{password}")
    User getInfo(@Param("username") String username, @Param("password") String password);

    int saveInfo(User user);

    @Select("select username, company_name, phone from user where user_id = #{id}")
    User getInfoById(@Param("id") Integer id);
}
