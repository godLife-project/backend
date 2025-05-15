package com.godLife.project.service.interfaces.AdminInterface;

import com.godLife.project.dto.categories.FaqCateDTO;
import com.godLife.project.dto.categories.QnaCateDTO;
import com.godLife.project.dto.categories.TopCateDTO;
import com.godLife.project.dto.datas.IconDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompSystemService {
  // FAQ 카테고리 관리
  List<FaqCateDTO> selectFaqCate();               // FAQ 카테고리 LIST 조회
  int insertFaqCate(FaqCateDTO faqCateDTO);       // FAQ 카테고리 추가
  int updateFaqCate(FaqCateDTO faqCateDTO);       // FAQ 카테고리 수정
  int deleteFaqCate(int faqCategoryIdx);          // FAQ 카테고리 삭제

  // QNA 카테고리 관리
  List<QnaCateDTO> selectQnaCate();               // QNA 카테고리 LIST 조회
  int insertQnaCate(QnaCateDTO qnaCateDTO);       // QNA 카테고리 추가
  int updateQnaCate(QnaCateDTO qnaCateDTO);       // QNA 카테고리 수정
  int deleteQnaCate(int categoryIdx);          // QNA 카테고리 삭제

  // Top Menu 관리
  List<TopCateDTO> selectTopMenu();
  int insertTopMenu(TopCateDTO topCateDTO);
  int updateTopMenu(TopCateDTO topCateDTO);
  int deleteTopMenu(int topIdx);
  void updateOrderTopMenu(List<TopCateDTO> orderedList); // 드래그 앤 드롭용

  // ICON 테이블 관리
  List<IconDTO> selectIcon();                // ICON  조회
  int insertIcon(IconDTO iconDTO);           // ICON  추가
  int updateIcon(IconDTO iconDTO);           // ICON  수정
  int deleteIcon(String iconKey);            // ICON  삭제

}
