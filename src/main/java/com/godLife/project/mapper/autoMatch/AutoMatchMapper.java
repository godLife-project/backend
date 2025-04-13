package com.godLife.project.mapper.autoMatch;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AutoMatchMapper {

  // 매칭 할 상담원 조회
  Integer getServiceAdminIdx();

  // 문의 등록 시 단일 문의 자동 매칭
  void autoMatchSingleQna(int qnaIdx, Integer adminIdx);

  // 매칭 성공 시 상담원 matched 숫자 증가
  void increaseMatchedCount(Integer adminIdx);
}
