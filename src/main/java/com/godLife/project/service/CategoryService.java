package com.godLife.project.service;

import com.godLife.project.dto.categories.TopCateDTO;

import java.util.List;

public interface CategoryService {
    List<TopCateDTO> getAllTopCategories();
}
