package com.godLife.project.service.interfaces;

import com.godLife.project.dto.contents.FaQDTO;
import com.godLife.project.dto.infos.SearchQueryDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FaqService {
  List<FaQDTO> selectAllFaq();
  FaQDTO getFaqById(Integer faqIdx);
  int createFaq(FaQDTO faq);
  int updateFaq(FaQDTO faq);
  int deleteFaq(@Param("faqIdx") int faqIdx);
  List<FaQDTO> searchFaq(SearchQueryDTO searchQuery);

}
