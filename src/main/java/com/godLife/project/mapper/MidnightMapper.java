package com.godLife.project.mapper;

import com.godLife.project.dto.scheduler.VerifyUnder90DTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MidnightMapper {

  // 활성화 된 모든 루틴 중 인증율 90 이하인 루틴 조회
  List<VerifyUnder90DTO> getPlanIdxIfVerifyRateIsUnder90();

  // 루틴 불꽃 경험치 감소 로직
  void decreaseCertExp(VerifyUnder90DTO verifyUnder90DTO);

  // 진행중인 루틴의 불꽃을 모두 활성화 못할 경우 콤보 초기화 로직
  @Update("UPDATE USER_TABLE SET COMBO = 0 WHERE USER_IDX = #{userIdx}")
  void clearCombo(int userIdx);

  // 불꽃 활성화 상태 초기화 로직
  @Update("UPDATE PLAN_TABLE SET FIRE_STATE = 0 WHERE IS_ACTIVE = 1 AND IS_COMPLETED = 0 AND FIRE_STATE = 1")
  void clearFireState();

  // 만료된 재발급 토큰 삭제 로직
  @Delete("DELETE FROM REFRESH_TOKEN WHERE EXPIRATION < SYSDATE")
  int deleteExpiredRefreshTokens();

  // 삭제 처리된 계정 완전 삭제
  int clearAccount(int expire);

  // REFRESH 토큰이 만료된 고객관리자 로그아웃 처리
  void clearServiceCenterByIsExpired();
}
