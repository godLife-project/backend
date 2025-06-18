package com.godLife.project.mapper;

import com.godLife.project.dto.contents.FaQDTO;
import com.godLife.project.dto.infos.SearchQueryDTO;
import com.godLife.project.dto.list.FaqListDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface FaqMapper {
  FaQDTO selectFaqById(@Param("faqIdx") Integer faqIdx);
  List<FaqListDTO> selectAllFaq();
  List<FaqListDTO> selectCateFaq(@Param("faqCategory")Integer faqCategory);
  int insertFaq(FaQDTO faq);
  int updateFaq(FaQDTO faq);

  @Delete("DELETE FROM FAQ_TABLE WHERE FAQ_IDX = #{faqIdx}")
  int deleteFaq(@Param("faqIdx") int faqIdx);

  // 검색
  List<FaQDTO> searchFaq(SearchQueryDTO searchQuery);
}
