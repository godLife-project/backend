package com.godLife.project.mapper;

import com.godLife.project.dto.list.PlanListDTO;
import com.godLife.project.dto.list.customDTOs.CustomPlanDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

  // 루틴의 리스트 정보 조회
  List<PlanListDTO> getAllPlanList(@Param("mode") String mode,
                                   @Param("offset") int offset,
                                   @Param("size") int size,
                                   @Param("status") int status,
                                   @Param("target") List<Integer> target,
                                   @Param("job") List<Integer> job,
                                   @Param("sort") String sort,
                                   @Param("order") String order);
  // 루틴 리스트 총 게시글 수 조회
  int getTotalPlanCount(@Param("mode") String mode,
                        @Param("status") int status,
                        @Param("target") List<Integer> target,
                        @Param("job") List<Integer> job);

}
