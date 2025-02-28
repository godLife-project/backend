package com.godLife.project.service;

import com.godLife.project.dto.datas.PlanDTO;

public interface PlanService {

  // 루틴과 활동 저장
  int insertPlanWithAct(PlanDTO planDTO);

  // 루틴과 활동 상세 조회
  PlanDTO detailRoutine(int planIdx);

  // 루틴과 활동 수정
  int modifyPlanWithAct(PlanDTO planDTO);

  // 루틴 삭제 처리
  int deletePlan(int planIdx, int userIdx);
}
