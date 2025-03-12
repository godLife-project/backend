package com.godLife.project.mapper;

import com.godLife.project.dto.request.VerifyRequestDTO;
import com.godLife.project.dto.verify.CheckAllFireActivateDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface VerifyMapper {
  // 활동 인증 처리
  @Insert("INSERT INTO VERIFY_TABLE(VERIFY_IDX, ACTIVITY_IDX, USER_IDX) VALUES (VERIFY_SEQ.NEXTVAL, #{activityIdx}, #{userIdx})")
  void verifyActivity(VerifyRequestDTO verifyRequestDTO);

  // 금일 인증 여부 확인
  @Select("SELECT COUNT(*) FROM VERIFY_TABLE WHERE ACTIVITY_IDX = #{activityIdx} AND VERIFY_DATE = TRUNC(SYSDATE)")
  boolean checkTodayVerified(VerifyRequestDTO verifyRequestDTO);

  // 활동 인증 시 루틴 경험치 증가
  @Update("UPDATE PLAN_TABLE SET CERT_EXP = CERT_EXP + 5 WHERE PLAN_IDX = #{planIdx} AND USER_IDX = #{userIdx}")
  void increaseCertEXP(VerifyRequestDTO verifyRequestDTO);
  // 경험치 증가 후 현재 경험치 백업
  @Update("UPDATE PLAN_TABLE SET LAST_EXP = CERT_EXP WHERE PLAN_IDX = #{planIdx} AND USER_IDX = #{userIdx}")
  void setLastEXP(VerifyRequestDTO verifyRequestDTO);

  // 루틴 인증율 계산
  double getVerifyRate(int planIdx);

  // 불꽃 활성화 처리
  @Update("UPDATE PLAN_TABLE SET FIRE_STATE = 1 WHERE PLAN_IDX = #{planIdx} AND USER_IDX = #{userIdx}")
  void setFireState(VerifyRequestDTO verifyRequestDTO);
  // 불꽃 활성화 여부 확인
  @Select("SELECT FIRE_STATE FROM PLAN_TABLE WHERE PLAN_IDX = #{planIdx}")
  boolean checkFireState(int planIdx);

  // 불꽃을 모두 활성화 했는지 확인
  List<CheckAllFireActivateDTO> checkAllFireIsActivateByUserIdx(int userIdx);

  // 유저 테이블 콤보 증가
  @Update("UPDATE USER_TABLE SET COMBO = COMBO + 1 WHERE USER_IDX = #{userIdx}")
  void increaseCombo(int userIdx);
  // 콤보 조회
  @Select("SELECT COMBO FROM USER_TABLE WHERE USER_IDX = #{userIdx}")
  int getComboByUserIdx(int userIdx);
  // 유저 테이블 경험치 증가
  @Update("UPDATE USER_TABLE SET USER_EXP = USER_EXP + #{exp} WHERE USER_IDX = #{userIdx}")
  void increaseUserExp(double exp, int userIdx);

}
