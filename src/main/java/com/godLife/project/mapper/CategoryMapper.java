package com.godLife.project.mapper;

import com.godLife.project.dto.categories.*;
import com.godLife.project.dto.datas.FireDTO;
import com.godLife.project.dto.datas.IconDTO;
import com.godLife.project.dto.datas.UserLevelDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 탑메뉴 카테고리 조회
    @Select("SELECT * FROM TOP_CATEGORY")
    List<TopCateDTO> getAllTopCategories();

    // 직업 카테고리 조회
    List<JobCateDTO> getAllJOBCategories();
    // 관심사 카테고리 조회
    List<TargetCateDTO> getAllTargetCategories();

    // 챌린지 카테고리 조회
    List<ChallengeCateDTO> getAllChallCategories();
    // 숏컷 카테고리
    @Select("SELECT * FROM SHORTCUT_CATEGORY")
    List<ShortCutCateDTO> getAllShortCategories();
    // 권한 카테고리 ( 관리자용 )
    @Select("SELECT * FROM AUTHORITY_CATEGORY")
    List<AuthorityCateDTO> getAllAuthorityCategories();

    // 아이콘 테이블 조회 ( 유저용 )
    @Select("SELECT * FROM ICON_TABLE WHERE VISIBLE = 1")
    List<IconDTO> getUserIconInfos();
    // 아이콘 테이블 조회 ( 관리자용 )
    @Select("SELECT * FROM ICON_TABLE")
    List<IconDTO> getAllIconInfos();

    // 불꽃 테이블 조회
    @Select("SELECT * FROM FIRE_TABLE")
    List<FireDTO> getAllFireInfos();
    // 유저 레벨 테이블 조회
    @Select("SELECT * FROM USER_LEVEL")
    List<UserLevelDTO> getAllUserLevelInfos();

    // qna 카테고리
    @Select("SELECT * FROM QNA_CATEGORY WHERE DEPRECATED = #{isDeprecated}")
    List<QnaCateDTO> getAllQnaCategories(boolean isDeprecated);

    // faq 카테고리
    @Select("SELECT * FROM FAQ_CATEGORY")
    List<FaqCateDTO> getAllFaQCategories();
}
