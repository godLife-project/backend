package com.godLife.project.mapper;

import com.godLife.project.dto.list.MyPlanDTO;
import com.godLife.project.dto.list.customDTOs.CustomPlanDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ListMapper {

  // 로그인한 유저의 진행/대기 중 루틴들 조회
  List<CustomPlanDTO> getMyPlansByUserIdx(int userIdx);
  // 루틴의 목표(관심사) 인덱스 번호 조회
  @Select("SELECT TARGET_IDX FROM PLAN_TABLE WHERE PLAN_IDX = #{planIdx}")
  int getTargetIdxByPlanIdx(int plansIdx);
  // 루틴의 직업 인덱스 번호 조회
  @Select("SELECT JOB_IDX FROM PLAN_TABLE WHERE PLAN_IDX = #{planIdx}")
  int getJobIdxByPlanIdx(int plansIdx);
}
