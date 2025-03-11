package com.godLife.project.service.impl;

import com.godLife.project.dto.request.VerifyRequestDTO;
import com.godLife.project.mapper.PlanMapper;
import com.godLife.project.mapper.VerifyMapper;
import com.godLife.project.service.VerifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Service
public class VerifyServiceImpl implements VerifyService {

  private final VerifyMapper verifyMapper;

  private final PlanMapper planMapper;

  public VerifyServiceImpl (VerifyMapper verifyMapper, PlanMapper planMapper) {
    this.verifyMapper = verifyMapper;
    this.planMapper = planMapper;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public int verifyActivity(VerifyRequestDTO verifyRequestDTO) {
    try {
      int planIdx = verifyRequestDTO.getPlanIdx();
      int activityIdx = verifyRequestDTO.getActivityIdx();
      int userIdx = verifyRequestDTO.getUserIdx();

      if (!planMapper.checkPlanByPlanIdx(planIdx, 0)) { return  404; } // not found 루틴 없음
      if (!planMapper.checkActByActivityIdx(planIdx, activityIdx)) { return 404; } // not found 활동 없음
      if (planMapper.getUserIdxByPlanIdx(planIdx) != verifyRequestDTO.getUserIdx()) { return 403; } // another 작성자 아님
      if (!planMapper.checkActiveByPlanIdx(planIdx)) { return 412; } // preCondition 루틴 활성화 상태 아님
      if (verifyMapper.checkTodayVerified(verifyRequestDTO)) { return 409; } // conflict 이미 인증 함 (activityIdx 사용)

      // 활동 인증 처리 ( 데이터 저장 )
      verifyMapper.verifyActivity(verifyRequestDTO); // activityIdx , userIdx 사용
      // 루틴 불꽃 경험치 증가 로직
      verifyMapper.increaseCertEXP(verifyRequestDTO); // planIdx, userIdx 사용
      // 루틴 마지막 인증 경험치 백업
      verifyMapper.setLastEXP(verifyRequestDTO); // planIdx, userIdx 사용
      // 루틴 인증율 확인
      long verifyRate = verifyMapper.getVerifyRate(planIdx);
      //System.out.println(verifyRate);
      if (verifyRate >= 80.0 && !verifyMapper.checkFireState(planIdx)) {
        verifyMapper.setFireState(verifyRequestDTO); // planIdx, userIdx 사용
        //System.out.println("불꽃 활성화");
        if (verifyMapper.checkAllFireIsActivateByUserIdx(userIdx)) { // 불꽃 모두 활성화 시 combo 증가
          //System.out.println("콤보 증가");
          verifyMapper.increaseCombo(userIdx);
        }
        // 유저 경험치 증가
        int combo = verifyMapper.getComboByUserIdx(userIdx);
        //System.out.println(getUserExpByCombo(combo));
        verifyMapper.increaseUserExp(getUserExpByCombo(combo), userIdx);
      }
      return 200; // ok
    } catch (Exception e) {
      log.error("Error verifying plan: ", e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 롤백
      return 500; // 서버 오류
    }
  }


  // 유저 경험치 계산 함수
  public double getUserExpByCombo(int combo) {
    double defaultExp = 50; // 기본 경험치
    int percentage = 10; // 퍼센티지
    int quotient = combo / 10; // 몫

    if (combo == 0 || quotient == 0) { return defaultExp; } // 콤보가 0이거나 10 보다 작을 경우 기본 경험치

    double bonusExp = defaultExp * ((quotient * percentage) * 0.01);

    return defaultExp + bonusExp;
  }
}
