package com.godLife.project.controller;

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

    @GetMapping("/topMenu")
    public List<TopCateDTO> topMenu() {
        return categoryService.getAllTopCategories();
    }
}
