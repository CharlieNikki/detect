package com.example.detect.mapper;

import com.example.detect.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    User getInfo(String phone, String password);

    int saveInfo(User user);

    User getInfoById(@Param("id") Integer id);
}
