package com.godLife.project.mapper;

import com.godLife.project.dto.categories.TopCateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Select("SELECT * FROM TOP_CATEGORY")
    List<TopCateDTO> getAllTopCategories();
}
