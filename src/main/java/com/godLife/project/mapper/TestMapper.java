package com.godLife.project.mapper;

import com.godLife.project.dto.TestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestMapper {
    @Select("SELECT JOB_NAME FROM JOB_CATEGORY")
    List<String> selectAll();
}
