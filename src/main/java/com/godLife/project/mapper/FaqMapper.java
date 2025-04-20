package com.godLife.project.mapper;

import com.godLife.project.dto.contents.FaQDTO;
import com.godLife.project.dto.list.FaqListDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FaqMapper {
  FaQDTO selectFaqById(@Param("faqIdx") Integer faqIdx);
  List<FaqListDTO> selectAllFaq();
  int insertFaq(FaQDTO faq);
  int updateFaq(FaQDTO faq);
  List<FaQDTO> searchFaq(@Param("query") String query);

  @Delete("DELETE FROM FAQ_TABLE WHERE FAQ_IDX = #{faqIdx}")
  int deleteFaq(@Param("faqIdx") int faqIdx);
}
