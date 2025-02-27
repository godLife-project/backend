package com.godLife.project.service.impl;

import com.godLife.project.dto.datas.ActivityDTO;
import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.mapper.PlanMapper;
import com.godLife.project.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Service
public class PlanServicelmpl implements PlanService {

  private final PlanMapper planMapper;

  public PlanServicelmpl(PlanMapper planMapper) {
    this.planMapper = planMapper;
  }

  // 루틴 작성 로직
  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean insertPlanWithAct(PlanDTO planDTO) {
    try {
      //System.out.println(planDTO);
      // 루틴 삽입하기
      planMapper.insertPlan(planDTO);

      int planIdx = planDTO.getPlanIdx();


      // 해당 루틴의 활동 삽입
      for (ActivityDTO activityDTO : planDTO.getActivities()) {
        activityDTO.setPlanIdx(planIdx);
        planMapper.insertActivity(activityDTO);
      }
      return true;
    } catch (Exception e) {
      log.error("e: ", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 수동 롤백
      return false;
    }
  }

  @Override
  @Transactional
  public PlanDTO detailRoutine(int planIdx) {
    // 루틴 조회
    PlanDTO planDTO = planMapper.detailPlanByPlanIdx(planIdx);
    // 활동 조회
    planDTO.setActivities(planMapper.detailActivityByPlanIdx(planIdx));

    return planDTO;
  }



}
