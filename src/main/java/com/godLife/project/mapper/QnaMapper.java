package com.godLife.project.mapper;

import com.godLife.project.dto.contents.QnaDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QnaMapper {

  // 1:1 문의 작성
  void createQna(QnaDTO qnaDTO);
}
