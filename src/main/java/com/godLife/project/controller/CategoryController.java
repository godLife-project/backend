package com.godLife.project.controller;

import com.godLife.project.dto.categories.*;
import com.godLife.project.dto.datas.FireDTO;
import com.godLife.project.dto.datas.IconDTO;
import com.godLife.project.dto.datas.UserLevelDTO;
import com.godLife.project.dto.response.qna.QnaParent;
import com.godLife.project.dto.response.top.TopMenu;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.interfaces.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final RedisService redisService;

    /// ================================== 공용 혹은 유저 전용 =============================================
    // 탑메뉴 카테고리
    @Operation(summary = "카테고리 조회", description = "탑 메뉴")
    @GetMapping("/topMenu")
    public List<TopMenu> topMenu() {
        List<TopMenu> data = redisService.getListData("category::topMenu", TopMenu.class);
        if (data == null || data.isEmpty()) {
            return categoryService.getProcessedAllTopCategories();
        }
        return data;
    }
    // 직업 카테고리
    @Operation(summary = "카테고리 조회", description = "직업")
    @GetMapping("/job")
    public List<JobCateDTO> job() { return categoryService.getAllJobCategories(); }
    // 관심사 카테고리
    @Operation(summary = "카테고리 조회", description = "관심사")
    @GetMapping("/target")
    public List<TargetCateDTO> target() { return categoryService.getAllTargetCategories(); }
    // 챌린지 카테고리
    @Operation(summary = "카테고리 조회", description = "챌린지")
    @GetMapping("/challenge")
    public List<ChallengeCateDTO> challenge() { return categoryService.getAllChallCategories(); }
    // 아이콘 카테고리
    @Operation(summary = "카테고리 조회", description = "아이콘")
    @GetMapping("/icon")
    public List<IconDTO> icon() { return categoryService.getUserIconInfos(); }
    // QNA 카테고리
    @Operation(summary = "카테고리 조회", description = "QNA")
    @GetMapping("/qna")
    public List<QnaParent> qna() { return categoryService.getAllQnaCategories();}
    /// ====================================================================================================
    /// ================================= DEPRECATED (사용 안 함) ==========================================
    // 숏컷 카테고리(사용안함)
    @Operation(summary = "카테고리 조회", description = "숏컷")
    @GetMapping("/shortcut")
    @Deprecated
    public List<ShortCutCateDTO> shortcut() { return categoryService.getAllShortCategories(); }
    /// ====================================================================================================
    /// ================================= ADMIN (관리자 권한 필수) ==========================================
    // 권한 카테고리
    @Operation(summary = "카테고리 조회", description = "권한")
    @GetMapping("/admin/authority")
    public List<AuthorityCateDTO> authority() { return categoryService.getAllAuthorityCategories(); }

    // 아이콘 카테고리
    @Operation(summary = "카테고리 조회", description = "아이콘 (관리자)")
    @GetMapping("/admin/icon")
    public List<IconDTO> iconAdmin() { return categoryService.getAllIconInfos(); }

    // 불꽃 정보 조회
    @GetMapping("/admin/fireInfos")
    public List<FireDTO> fireInfos() { return categoryService.getAllFireInfos(); }

    // 유저 레벨 정보 조회
    @GetMapping("/admin/userLevelInfos")
    public List<UserLevelDTO> userLevelInfos() {return categoryService.getAllUserLevelInfos(); }
    /// ====================================================================================================


}
