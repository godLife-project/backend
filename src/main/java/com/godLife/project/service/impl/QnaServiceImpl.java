package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.exception.CustomException;
import com.godLife.project.mapper.QnaMapper;
import com.godLife.project.service.interfaces.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QnaServiceImpl implements QnaService {

  private final QnaMapper qnaMapper;

  @Override
  public void createQna(QnaDTO qnaDTO) {
    try {
      qnaMapper.createQna(qnaDTO);
    } catch (Exception e) {
      log.error("QnaService - createQna :: 1:1 문의 저장 중 오류 발생: ", e);
      throw new CustomException("DB 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
