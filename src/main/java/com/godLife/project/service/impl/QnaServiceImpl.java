package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.QnADTO;
import com.godLife.project.dto.contents.QnaContentDTO;
import com.godLife.project.mapper.QnaMapper;
import com.godLife.project.service.interfaces.QnaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
public class QnaServiceImpl implements QnaService {
  private final QnaMapper qnaMapper;
  public  QnaServiceImpl(QnaMapper qnaMapper) {this.qnaMapper = qnaMapper;}

  // 질문 추가

  public void insertQuestion(QnADTO qnaDTO) {
    qnaMapper.insertQuestion(qnaDTO);
  }

  // 질문 내용 추가 (답변)
  public void insertQnaContent(QnaContentDTO qnaContentDTO) {
    qnaMapper.insertQnaContent(qnaContentDTO);
  }

  // 특정 질문 가져오기
  public QnADTO selectQuestionById(Long qnaIdx) {
    return qnaMapper.selectQuestionById(qnaIdx);
  }

  // 질문의 대한 대화 목록 가져오기 (채팅 기록)
  public List<QnaContentDTO> getQnaContent(Long qnaIdx) {
    return qnaMapper.getQnaContent(qnaIdx);  // QNA_CONTENT 테이블에서 메시지 조회
  }
}
