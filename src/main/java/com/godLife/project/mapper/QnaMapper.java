package com.godLife.project.mapper;

import com.godLife.project.dto.contents.QnADTO;
import com.godLife.project.dto.contents.QnaContentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QnaMapper {

  // 질문 추가
  void insertQuestion(QnADTO qnaDTO);

  // 질문 내용 추가
  void insertQnaContent(QnaContentDTO qnaContentDTO);

  // 특정 질문 가져오기
  QnADTO selectQuestionById(@Param("qnaIdx") Long qnaIdx);

  // 질문의 대한 메세지 목록 가져오기
  List<QnaContentDTO> getQnaContent(@Param("questionIdx") Long questionIdx);
}
