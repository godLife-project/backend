package com.godLife.project.service;

import com.godLife.project.dto.categories.*;
import com.godLife.project.dto.datas.IconDTO;

import java.util.List;

public interface CategoryService {
    // 탑메뉴 카테고리 조회
    List<TopCateDTO> getAllTopCategories();
    // 직업 카테고리 조회
    List<JobCateDTO> getAllJobCategories();
    // 관심사 카테고리 조회
    List<TargetCateDTO> getAllTargetCategories();
    // 챌린지 카테고리 조회
    List<ChallengeCateDTO> getAllChallCategories();
    // 숏컷 카테고리
    List<ShortCutCateDTO> getAllShortCategories();
    // 권한 카테고리
    List<AuthorityCateDTO> getAllAuthorityCategories();
    // 아이콘 정보 조회
    List<IconDTO> getUserIconInfos();
    // 아이콘 정보 조회
    List<IconDTO> getAllIconInfos();

}
