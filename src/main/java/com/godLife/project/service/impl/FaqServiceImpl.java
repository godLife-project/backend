package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.FaQDTO;
import com.godLife.project.dto.infos.SearchQueryDTO;
import com.godLife.project.mapper.FaqMapper;
import com.godLife.project.service.interfaces.FaqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Transactional
@Service
public class FaqServiceImpl implements FaqService {
  private final FaqMapper faqMapper;
  public FaqServiceImpl(FaqMapper faqMapper) {this.faqMapper = faqMapper;}


  // 특정 FAQ를 Idx로 조회
  public FaQDTO getFaqById(Integer faqIdx) {
    return faqMapper.selectFaqById(faqIdx);
  }
  // 모든 FAQ 조회
  public List<FaQDTO> selectAllFaq() {
    return faqMapper.selectAllFaq();
  }


  // FAQ 작성
  public int createFaq(FaQDTO faq) {

    int result = faqMapper.insertFaq(faq);
    return (result > 0) ? 200 : 500; // 성공: 200, 실패: 500
  }

  //  FAQ 수정
  public int updateFaq(FaQDTO faq) {
    faqMapper.updateFaq(faq);
    try {
      // FAQ 수정
      return 200; // 성공
    } catch (Exception e) {
      log.error("Error modifying FAQ: ", e);
      return 500; // Internal Server Error: 수정 실패
    }
  }

  // FAQ 삭제
  public int deleteFaq(int faqIdx) {

    try {
      // FAQ 삭제
      faqMapper.deleteFaq(faqIdx);
      return 200; // 성공
    } catch (Exception e) {
      // 오류 발생 시 처리
      return 500; // Internal Server Error
    }
  }


  // FAQ 검색
  public List<FaQDTO> searchFaq(SearchQueryDTO searchQuery) {
    String query = searchQuery.getQuery();
    if (query == null || query.trim().isEmpty()) {
      return Collections.emptyList();
    }
    return faqMapper.searchFaq(query.trim()); // 공백 제거 후 검색
  }

}
