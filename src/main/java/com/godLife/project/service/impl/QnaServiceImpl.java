package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.dto.qnaWebsocket.QnaMatchedListDTO;
import com.godLife.project.dto.qnaWebsocket.QnaWaitListDTO;
import com.godLife.project.dto.qnaWebsocket.WaitListMessageDTO;
import com.godLife.project.exception.CustomException;
import com.godLife.project.exception.UnauthorizedException;
import com.godLife.project.exception.WebSocketBusinessException;
import com.godLife.project.mapper.QnaMapper;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.interfaces.QnaService;
import com.godLife.project.utils.HtmlSanitizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

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

  // 대기중인 문의 리스트 조회 (전체)
  @Override
  public WaitListMessageDTO getlistAllWaitQna(String chatRoomNo) {
    try {
      List<QnaWaitListDTO> waitList = qnaMapper.getlistAllWaitQna();
      WaitListMessageDTO request = new WaitListMessageDTO();
      request.setWaitQnaInfos(waitList);
      request.setRoomNo(chatRoomNo);

      return request;
    } catch (Exception e) {
      throw new WebSocketBusinessException("대기중인 QnA 리스트를 불러오는 중 오류 발생", 5001);
    }
  }

  // 매칭된 문의 리스트 조회 (개인)
  @Override
  public List<QnaMatchedListDTO> getlistAllMatchedQna(int adminIdx) {
    try {
      return qnaMapper.getlistAllMatchedQna(adminIdx);
    } catch (Exception e) {
      throw new WebSocketBusinessException("매칭된 QnA 리스트를 불러오는 중 오류 발생", 5001);
    }
  }

  // qna 본문 조회
  @Override
  public String getQnaContent(int qnaIdx) {
    try {
      String rawContent = qnaMapper.getQnaContent(qnaIdx);

      if (rawContent == null || rawContent.isBlank()) {
        throw new CustomException("조회하려는 문의가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
      }

      return HtmlSanitizer.sanitize(rawContent);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error("QnaService - getQnaContent :: 문의 본문 조회 중 오류 발생: ", e);
      throw new CustomException("DB 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
