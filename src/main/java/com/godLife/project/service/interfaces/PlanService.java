package com.godLife.project.service.interfaces;

import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.dto.request.PlanRequestDTO;

public interface PlanService {

  // 루틴과 활동 저장
  int insertPlanWithAct(PlanDTO planDTO);

  // 루틴과 활동 상세 조회
  PlanDTO detailRoutine(int planIdx, int isDeleted);

  // 루틴과 활동 수정
  int modifyPlanWithAct(PlanDTO planDTO, int isDeleted);

  // 루틴 삭제 처리
  int deletePlan(int planIdx, int userIdx);

  // 루틴 비/활성화
  int goStopPlan(int planIdx, int userIdx, int isActive, int isDeleted);

  // 루틴 추천
  int likePlan(int planIdx, int userIdx, int isDeleted);

  // 루틴 추천 여부 조회
  boolean checkLike(int planIdx, int userIdx);

  // 루틴 추천 취소
  int unLikePlan(int planIdx, int userIdx);

  // 루틴 조회수 상승
  void increaseView(int planIdx);

  // 루틴 조기 완료
  int updateEarlyComplete(int planIdx, int userIdx);

  // 후기 작성
  int addReview(PlanRequestDTO planRequestDTO);

  // 후기 작성
  int modifyReview(PlanRequestDTO planRequestDTO);
}
