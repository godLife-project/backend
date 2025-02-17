package com.godLife.project.mapper;

import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import com.godLife.project.dto.categories.TopCateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 탑메뉴 카테고리 조회
    @Select("SELECT * FROM TOP_CATEGORY")
    List<TopCateDTO> getAllTopCategories();
    // 직업 카테고리 조회
    @Select("SELECT * FROM JOB_CATEGORY")
    List<JobCateDTO> getAllJOBCategories();
    // 관심사 카테고리 조회
    @Select("SELECT * FROM TARGET_CATEGORY")
    List<TargetCateDTO> getAllTargetCategories();
}
