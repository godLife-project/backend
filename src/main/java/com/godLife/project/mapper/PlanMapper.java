package com.godLife.project.mapper;

import com.godLife.project.dto.datas.ActivityDTO;
import com.godLife.project.dto.datas.PlanDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}