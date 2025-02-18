package com.godLife.project.mapper;

import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.datas.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestMapper {
    @Select("SELECT JOB_NAME FROM JOB_CATEGORY")
    List<String> selectAll();

    @Select("SELECT * FROM USER_TABLE WHERE USER_IDX = #{userIdx}")
    UserDTO findById(int userIdx);

    List<UserDTO> findAll();

    @Select("SELECT * FROM JOB_CATEGORY")
    List<JobCateDTO> selectJobAll();
}
