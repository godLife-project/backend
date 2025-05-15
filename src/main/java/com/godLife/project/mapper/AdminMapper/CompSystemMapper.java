package com.godLife.project.mapper.AdminMapper;

import com.godLife.project.dto.categories.FaqCateDTO;
import com.godLife.project.dto.categories.QnaCateDTO;
import com.godLife.project.dto.categories.TopCateDTO;
import com.godLife.project.dto.contents.FaQDTO;
import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.dto.datas.IconDTO;
import com.godLife.project.dto.list.QnaListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompSystemMapper {

  // FAQ 카테고리 관리
  List<FaqCateDTO> selectAllFaqCate();      // FAQ 카테고리 조회
  List<FaQDTO> faqListByCategory(int faqCategoryIdx); // 카테고리별 조회
  int insertFaqCate(FaqCateDTO faqCateDTO); // FAQ 카테고리 추가
  int updateFaqCate(FaqCateDTO faqCateDTO); // FAQ 카테고리 수정
  int deleteFaqByCate(int faqCategoryIdx);  // FAQ 삭제 (카테고리 삭제시)
  int deleteFaqCate(int faqCategoryIdx);    // FAQ 카테고리 삭제
  int countFaqByCategory(int faqCategoryIdx);   // FAQ 카테고리 참조 조회
  int countByFaqName(String faqCategoryName);   // FAQ 카테고리명 중복체크

  // QNA 카테고리 관리
  List<QnaCateDTO> selectAllQnaCate();      // QNA 카테고리 조회
  List<QnaListDTO> qnaListByCategory(@Param("category") int category); // 카테고리별 조회
  int insertQnaCate(QnaCateDTO qnaCateDTO); // QNA 카테고리 추가
  int updateQnaCate(QnaCateDTO qnaCateDTO); // QNA 카테고리 수정
  int deleteQnaByCate(int categoryIdx);     // QNA 해당 카테고리 데이터 삭제
  int deleteQnaCate(int categoryIdx);    // QNA 카테고리 삭제
  int countQnaByCategory(int categoryIdx);   // QNA 카테고리 참조 조회
  int countQnaChildCategories(int categoryIdx); // 자식 카테고리 존재 여부 조회
  int deleteChildQnaCategories(int parentIdx);  // 자식 카테고리 삭제
  int countByQnaName(String categoryIdx);   // QNA 카테고리명 중복체크

  // Top Menu 카테고리 관리
  List<TopCateDTO> selectTopMenu();             // TopMenu 카테고리 조회
  int insertTopMenu(TopCateDTO dto);               // TopMenu 카테고리 추가
  int updateTopMenu(TopCateDTO dto);               // TopMenu 카테고리 수정
  int deleteTopMenu(int topIdx);                   // TopMenu 카테고리 삭제
  int updateOrderTopMenu(@Param("topIdx") int topIdx, @Param("ordIdx") int ordIdx);    // TopMenu 카테고리 재배열
  int countByTopMenuName(String topName);         // TopMenu 카테고리명 중복체크

  // ICON 테이블 관리
  List<IconDTO> selectIcon();             // ICON  조회
  int insertIcon(IconDTO iconDTO);               // ICON  추가
  int updateIcon(IconDTO iconDTO);               // ICON  수정
  int deleteIcon(String iconKey);                   // ICON  삭제
  int countByIconName(String iconKey);         // ICON  중복체크
  int countByIconKeyExcludingSelf(@Param("newKey") String newKey, @Param("originalKey") String originalKey); // 자기 자신 제외
}
