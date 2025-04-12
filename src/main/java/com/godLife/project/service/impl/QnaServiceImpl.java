package com.godLife.project.service.impl;

import com.godLife.project.dto.contents.QnaDTO;
import com.godLife.project.exception.CustomException;
import com.godLife.project.mapper.QnaMapper;
import com.godLife.project.mapper.autoMatch.AutoMatchMapper;
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
  private final AutoMatchMapper autoMatchMapper;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void createQna(QnaDTO qnaDTO) {
    try {
      qnaMapper.createQna(qnaDTO); // 문의 저장
      Integer adminIdx = autoMatchMapper.getServiceAdminIdx(); // 매칭 가능 상담원 조회
      if (adminIdx != null) {
        Integer waitQnaIdx = autoMatchMapper.getAnotherWaitQnaIdx(qnaDTO.getQnaIdx()); // 다른 대기중 문의 확인
        if (waitQnaIdx != null) {
          autoMatchMapper.autoMatchSingleQna(waitQnaIdx, adminIdx); // 상담원 자동 매칭 시도
          autoMatchMapper.increaseMatchedCount(adminIdx); // 매칭된 상담원 매칭 문의 수 증가
          log.info("QnaService - createQna :: 대기중 인 문의 먼저 매칭되었습니다. ::> 매칭된 문의 - {} / 담당자 - {}", waitQnaIdx, adminIdx);
          log.info("QnaService - createQna :: 문의 자동 매칭이 보류 되었습니다. ::> 보류된 문의 - {}", qnaDTO.getQnaIdx());
        }
        else {
          autoMatchMapper.autoMatchSingleQna(qnaDTO.getQnaIdx(), adminIdx); // 상담원 자동 매칭 시도
          autoMatchMapper.increaseMatchedCount(adminIdx); // 매칭된 상담원 매칭 문의 수 증가
          log.info("QnaService - createQna :: 문의 담당자에게 매칭되었습니다. ::> 매칭된 문의 - {} / 담당자 - {}", qnaDTO.getQnaIdx() ,adminIdx);
        }
      }
      else {
        log.info("QnaService - createQna :: 매칭 가능한 문의 담당자가 없습니다. 대기 상태로 저장됩니다.");
      }

    } catch (Exception e) {
      log.error("QnaService - createQna :: 1:1 문의 저장 중 오류 발생: ", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
      throw new CustomException("DB 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
