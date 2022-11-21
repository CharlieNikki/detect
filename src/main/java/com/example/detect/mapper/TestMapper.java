package com.example.detect.mapper;

import com.example.detect.entity.Test;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestMapper {

    @Insert("insert into test(image) values(#{image})")
    int insertImage(Test test);

    @Select("select * from test where id = #{id}")
    Test selectTestById(Integer id);
}
