package com.godLife.project.service;

import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import com.godLife.project.dto.categories.TopCateDTO;
import com.godLife.project.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    // 탑메뉴 카테고리 조회
    @Override
    public List<TopCateDTO> getAllTopCategories() {
        return categoryMapper.getAllTopCategories();
    }
    // 직업 카테고리 조회
    @Override
    public List<JobCateDTO> getAllJobCategories() {return categoryMapper.getAllJOBCategories(); }
    // 관심사 카테고리 조회
    @Override
    public  List<TargetCateDTO> getAllTargetCategories() { return categoryMapper.getAllTargetCategories(); }
}
