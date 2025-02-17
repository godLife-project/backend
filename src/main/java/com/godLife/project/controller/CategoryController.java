package com.godLife.project.controller;

import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import com.godLife.project.dto.categories.TopCateDTO;
import com.godLife.project.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 탑메뉴 카테고리
    @GetMapping("/topMenu")
    public List<TopCateDTO> topMenu() {
        return categoryService.getAllTopCategories();
    }
    // 직업 카테고리
    @GetMapping("/job")
    public List<JobCateDTO>  job() { return categoryService.getAllJobCategories(); }
    // 관심사 카테고리
    @GetMapping("/target")
    public List<TargetCateDTO> target() { return categoryService.getAllTargetCategories(); }
}
