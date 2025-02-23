package com.godLife.project.service;

import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import com.godLife.project.dto.categories.TopCateDTO;

import java.util.List;

public interface CategoryService {
    // 탑메뉴 카테고리 조회
    List<TopCateDTO> getAllTopCategories();
    // 직업 카테고리 조회
    List<JobCateDTO> getAllJobCategories();
    // 관심사 카테고리 조회
    List<TargetCateDTO> getAllTargetCategories();

}
