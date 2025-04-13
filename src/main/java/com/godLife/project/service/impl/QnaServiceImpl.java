package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.exception.CustomException;
import com.godLife.project.mapper.QnaMapper;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.interfaces.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class QnaServiceImpl implements QnaService {

  private final QnaMapper qnaMapper;
  private final RedisService redisService;

  private static final String QNA_QUEUE_KEY = "qna_queue";


  @Override
  @Transactional(rollbackFor = Exception.class)
  public void createQna(QnaDTO qnaDTO) {
    try {
      qnaMapper.createQna(qnaDTO); // 문의 저장
      redisService.leftPushToRedisQueue(QNA_QUEUE_KEY, String.valueOf(qnaDTO.getQnaIdx())); // 큐에 저장
      log.info("QnaService - createQna :: 문의 등록 후 큐에 추가됨 - {}", qnaDTO.getQnaIdx());

    } catch (Exception e) {
      log.error("QnaService - createQna :: 1:1 문의 저장 중 오류 발생: ", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
      throw new CustomException("DB 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
