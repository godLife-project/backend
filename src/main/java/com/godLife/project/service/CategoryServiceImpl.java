package com.godLife.project.service;

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

    @Override
    public List<TopCateDTO> getAllTopCategories() {
        return categoryMapper.getAllTopCategories();
    }
}
