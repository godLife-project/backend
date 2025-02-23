package com.godLife.project.controller;

import com.godLife.project.dto.categories.*;
import com.godLife.project.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "카테고리 조회", description = "탑 메뉴")
    @GetMapping("/topMenu")
    public List<TopCateDTO> topMenu() {
        return categoryService.getAllTopCategories();
    }
    // 직업 카테고리
    @Operation(summary = "카테고리 조회", description = "직업")
    @GetMapping("/job")
    public List<JobCateDTO>  job() { return categoryService.getAllJobCategories(); }
    // 관심사 카테고리
    @Operation(summary = "카테고리 조회", description = "관심사")
    @GetMapping("/target")
    public List<TargetCateDTO> target() { return categoryService.getAllTargetCategories(); }

}
