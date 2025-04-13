package com.godLife.project.mapper;

import com.godLife.project.dto.contents.QnaDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QnaMapper {

  // 1:1 문의 작성
  void createQna(QnaDTO qnaDTO);

  // 'WAIT' 상태 문의의 qnaIdx 가져오기
  @Select("SELECT QNA_IDX FROM QNA_TABLE WHERE QNA_STATUS = 'WAIT'")
  List<Integer> getlistWaitQnaIdx();
}
