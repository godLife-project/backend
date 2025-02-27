package com.godLife.project.service;

import com.godLife.project.dto.datas.PlanDTO;

public interface PlanService {

  // 루틴과 활동 저장
  boolean insertPlanWithAct(PlanDTO planDTO);

  // 루틴과 활동 상세 조회
  PlanDTO detailRoutine(int planIdx);
}
