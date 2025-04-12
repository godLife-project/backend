package com.godLife.project.mapper.autoMatch;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AutoMatchMapper {

  // 매칭 할 상담원 조회
  Integer getServiceAdminIdx();

  // 신규 문의 제외 다른 대기중인 문의 있는지 확인
  Integer getAnotherWaitQnaIdx(int qnaIdx);
  // 문의 등록 시 단일 문의 자동 매칭
  void autoMatchSingleQna(int qnaIdx, Integer adminIdx);

  // 매칭 성공 시 상담원 matched 숫자 증가
  void increaseMatchedCount(Integer adminIdx);
}
