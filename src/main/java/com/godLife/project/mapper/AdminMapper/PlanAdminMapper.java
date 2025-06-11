package com.godLife.project.mapper.AdminMapper;

import com.godLife.project.dto.categories.JobEtcCateDTO;
import com.godLife.project.dto.datas.ActivityDTO;
import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.dto.list.customDTOs.CustomAdminPlanListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlanAdminMapper {
  // 관리자 루틴 리스트 조회 (페이징 처리)
  List<CustomAdminPlanListDTO> selectAdminPlanList(@Param("offset") int offset, @Param("limit") int limit);
  // 전체 루틴 개수 조회
  int getTotalAdminPlanCount();
  // 관리자 루틴 카테고리 조회 (페이징 처리)
  List<CustomAdminPlanListDTO> selectAdminPlanListByTargetIdx(
          @Param("targetIdx") Integer targetIdx,
          @Param("offset") int offset, @Param("limit") int limit);
  List<CustomAdminPlanListDTO> selectPlanList (@Param("offset") int offset, @Param("limit") int limit);

  int adminInsertPlan(PlanDTO planDTO);

  // 루틴 횟수 조회
  @Select("SELECT COUNT(*) FROM PLAN_TABLE WHERE USER_IDX = #{userIdx} AND IS_COMPLETED = #{isCompleted} AND IS_DELETED = #{isDeleted}")
  int getCntOfPlanByUserIdxNIsCompleted(int userIdx, int isCompleted, int isDeleted);

  // 유저 탈퇴 여부 확인
  @Select("SELECT IS_DELETED FROM USER_TABLE WHERE USER_IDX = #{userIdx}")
  String getUserIsDeleted(int userIdx);

  // 활동 추가
  void adminInsertActivity(ActivityDTO activityDTO);

  // 기타 직업 추가
  void insertJobEtc(JobEtcCateDTO jobEtcCateDTO);

  // 포크수 업데이트
  void modifyForkCount(int planIdx, int isDeleted);


}
