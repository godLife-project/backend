package com.godLife.project.controller;

import com.godLife.project.dto.categories.*;
import com.godLife.project.dto.datas.IconDTO;
import com.godLife.project.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

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
    // 챌린지 카테고리
    @Operation(summary = "카테고리 조회", description = "챌린지")
    @GetMapping("/challenge")
    public List<ChallengeCateDTO> challenge() { return categoryService.getAllChallCategories(); }
    // 숏컷 카테고리
    @Operation(summary = "카테고리 조회", description = "숏컷")
    @GetMapping("/shortcut")
    public List<ShortCutCateDTO> shortcut() { return categoryService.getAllShortCategories(); }
    // 권한 카테고리
    @Operation(summary = "카테고리 조회", description = "권한")
    @PostMapping("/auth/authority")
    public List<AuthorityCateDTO> authority() { return categoryService.getAllAuthorityCategories(); }
    // 아이콘 카테고리
    @Operation(summary = "카테고리 조회", description = "아이콘")
    @GetMapping("/icon")
    public List<IconDTO> icon() { return categoryService.getUserIconInfos(); }

    // 아이콘 카테고리
    @Operation(summary = "카테고리 조회", description = "아이콘 (관리자)")
    @PostMapping("/auth/icon")
    public List<IconDTO> iconAdmin() { return categoryService.getAllIconInfos(); }

}
