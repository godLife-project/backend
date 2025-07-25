package com.godLife.project.service.interfaces;

import com.godLife.project.dto.categories.*;
import com.godLife.project.dto.datas.FireDTO;
import com.godLife.project.dto.datas.IconDTO;
import com.godLife.project.dto.datas.UserLevelDTO;
import com.godLife.project.dto.response.qna.QnaParent;
import com.godLife.project.dto.response.top.TopMenu;

import java.util.List;

public interface CategoryService {
    // 탑메뉴 카테고리 조회 (데이터 가공)
    List<TopMenu> getProcessedAllTopCategories();
    // 탑메뉴 카테고리 조회 (데이터 원본)
    List<TopCateDTO> getOriginAllTopCategories();
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

    // QNA 카테고리
    List<QnaParent> getAllQnaCategories();

    // 불꽃 정보 조회
    List<FireDTO> getAllFireInfos();
    // 유저 레벨 정보 조회
    List<UserLevelDTO> getAllUserLevelInfos();

    // FAQ 카테고리
    List<FaqCateDTO> getAllFaQCategories();

}
