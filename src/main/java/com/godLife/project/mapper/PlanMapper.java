package com.godLife.project.mapper;

import com.godLife.project.dto.datas.ActivityDTO;
import com.godLife.project.dto.datas.PlanDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlanMapper {

  // 루틴 추가
  void insertPlan(PlanDTO planDTO);
  // 활동 추가
  void insertActivity(ActivityDTO activityDTO);

  // 루틴 상세 보기
  @Select("SELECT * FROM PLAN_TABLE WHERE PLAN_IDX = #{planIdx}")
  PlanDTO detailPlanByPlanIdx(int planIdx);
  // 활동 상세 보기
  @Select("SELECT * FROM PLAN_ACTIVITY WHERE PLAN_IDX = #{planIdx}")
  List<ActivityDTO> detailActivityByPlanIdx(int planIdx);

  // 작성자 인덱스 조회
  @Select("SELECT USER_IDX FROM PLAN_TABLE WHERE PLAN_IDX = #{planIdx}")
  int getUserIdxByPlanIDx(int planIdx);

  // 루틴 존재 여부 확인
  @Select("SELECT COUNT(*) FROM PLAN_TABLE WHERE PLAN_IDX = #{planIdx}")
  boolean checkPlanByPlanIdx(int planIdx);
  // 활동 존재 여부 확인
  @Select("SELECT COUNT(*) FROM PLAN_ACTIVITY WHERE PLAN_IDX = #{planIdx} AND ACTIVITY_IDX = #{activityIdx}")
  boolean checkActByActivityIdx(int planIdx, int activityIdx);

  // 루틴 수정하기
  void modifyPlan(PlanDTO planDTO);
  // 활동 수정하기
  void modifyActivity(ActivityDTO activityDTO);

  // 루틴 수정 시 활동 삭제
  @Delete("DELETE FROM PLAN_ACTIVITY WHERE ACTIVITY_IDX = #{activityIdx}")
  void deleteActByActivityIdx(int activityIdx);

  // 루틴 삭제 처리
  void deletePlan(@Param("planIdx") int planIdx,@Param("userIdx") int userIdx);
}