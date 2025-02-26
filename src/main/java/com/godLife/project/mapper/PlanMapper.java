package com.godLife.project.mapper;

import com.godLife.project.dto.datas.ActivityDTO;
import com.godLife.project.dto.datas.PlanDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanMapper {

  // 루틴 추가
  void insertPlan(PlanDTO planDTO);
  // 활동 추가
  void insertActivity(ActivityDTO activityDTO);
}