package com.godLife.project.service.impl.scheduleImpl;

import com.godLife.project.dto.scheduler.VerifyUnder90DTO;
import com.godLife.project.dto.verify.CheckAllFireActivateDTO;
import com.godLife.project.mapper.MidnightMapper;
import com.godLife.project.mapper.VerifyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineScheduleServiceImpl {

  private final MidnightMapper midnightMapper;
  private final VerifyMapper verifyMapper;

  // 루틴 인증 90 미만 루틴의 경험치 차감 로직
  public void verifyAndDecreaseExpIfUnder90() {
    // 루틴 인증율 90 미만 루틴 조회
    List<VerifyUnder90DTO> underDtos =  midnightMapper.getPlanIdxIfVerifyRateIsUnder90();

    for (VerifyUnder90DTO dto : underDtos) {
      double verifyRate = dto.getVerifyRate();

      if (80.0 <= verifyRate && verifyRate <= 89.0) {               // 80 ~ 89% : -5 EXP
        expDecreaseProcedure(dto, 5);
      } else if (50.0 <= verifyRate && verifyRate <= 79.0) {        // 50 ~ 79% : -10 EXP
        expDecreaseProcedure(dto, 10);
      } else if (1.0 <= verifyRate && verifyRate <= 49.0) {         // 1 ~ 49% : -15 EXP
        expDecreaseProcedure(dto, 15);
      } else {
        expDecreaseProcedure(dto, 20);             // 0% : -20 EXP
      }
    }
  }

  // 진행중인 루틴의 모든 불꽃 활성화 못한 유저의 콤보 초기화 로직
  public void clearComboWhenAllFireIsActivatedOfPlan() {
    // 모든 불꽃을 활성화 하지 못 한 유저 조회
    List<CheckAllFireActivateDTO> dtos = verifyMapper.checkAllFireIsActivateByUserIdx(0); // 0 : 모든 유저
    if (dtos.isEmpty()) { return; } // 없을 경우 로직 종료

    for (CheckAllFireActivateDTO dto : dtos) {
      midnightMapper.clearCombo(dto.getUserIdx()); // 콤보 초기화
    }
  }

  // 불꽃 활성화 상태 초기화 로직
  public void clearAllFireStateWhenMidnight() {
    midnightMapper.clearFireState();
  }

  // 만료된 재발급 토큰 삭제 로직
  public int deleteExpiredRefreshTokens() { return midnightMapper.deleteExpiredRefreshTokens(); }


  /* -----------------------------------------// 함수 구현 //------------------------------------------------------- */
  public void expDecreaseProcedure(VerifyUnder90DTO dto, int defaultMinusExp) {
    // 현재 경험치가 0 이면 감소 안 함.
    if (dto.getCertExp() <= 0) { return; }

    // 경험치가 defaultMinusExp 만큼 감소하면 0 미만일 때, 감소 가능한 만큼만 감소
    if (dto.getCertExp() - defaultMinusExp < 0) { dto.setMinusExp(dto.getCertExp()); }

    // 경험치 감소 전 누적 감소량이 expLimit을 초과할 경우 조정된 하락치 적용
    else if (dto.getLastExp() - (dto.getCertExp() - defaultMinusExp) > dto.getExpLimit()) {
      int minusExp = dto.getExpLimit() - (dto.getLastExp() - dto.getCertExp());

      if (minusExp < 0) { return; } // 하락치가 음수일 경우 감소 안 함

      dto.setMinusExp(minusExp);
    }
    // 기본 하락치 적용
    else { dto.setMinusExp(defaultMinusExp); }
    // 최종 경험치 감소 처리
    midnightMapper.decreaseCertExp(dto);
  }
/* --------------------------------------------------------------------------------------------------------------- */
}
