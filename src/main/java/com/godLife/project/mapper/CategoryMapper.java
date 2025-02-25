package com.godLife.project.mapper;

import com.godLife.project.dto.categories.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 탑메뉴 카테고리 조회
    @Select("SELECT * FROM TOP_CATEGORY")
    List<TopCateDTO> getAllTopCategories();
    // 직업 카테고리 조회
    @Select("SELECT * FROM JOB_CATEGORY")
    List<JobCateDTO> getAllJOBCategories();
    // 관심사 카테고리 조회
    @Select("SELECT * FROM TARGET_CATEGORY")
    List<TargetCateDTO> getAllTargetCategories();
    // 챌린지 카테고리 조회
    @Select("SELECT * FROM CHALL_CATEGORY")
    List<ChallengeCateDTO> getAllChallCategories();
    // 숏컷 카테고리
    @Select("SELECT * FROM SHORTCUT_CATEGORY")
    List<ShortCutCateDTO> getAllShortCategories();
    // 권한 카테고리
    @Select("SELECT * FROM AUTHORITY_CATEGORY")
    List<AuthorityCateDTO> getAllAuthorityCategories();
}
