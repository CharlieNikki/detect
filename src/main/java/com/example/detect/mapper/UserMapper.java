package com.example.detect.mapper;

import com.example.detect.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User getInfo(String phone, String password);

    int saveInfo(User user);

    User getInfoById(Integer id);

    int updateUserInfo(User user);
}
