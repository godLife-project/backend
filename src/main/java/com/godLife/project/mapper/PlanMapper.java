package com.godLife.project.mapper;

import com.godLife.project.dto.categories.JobCateDTO;
import com.godLife.project.dto.categories.JobEtcCateDTO;
import com.godLife.project.dto.categories.TargetCateDTO;
import com.godLife.project.dto.datas.ActivityDTO;
import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.dto.request.PlanRequestDTO;
import com.godLife.project.typehandler.ListStringTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlanMapper {

  // 루틴 추가
  void insertPlan(PlanDTO planDTO);
  // 활동 추가
  void insertActivity(ActivityDTO activityDTO);
  // 기타 직업 추가
  void insertJobEtc(JobEtcCateDTO jobEtcCateDTO);

  // 루틴 상세 보기
  @Select("SELECT * FROM PLAN_TABLE WHERE PLAN_IDX = #{planIdx} AND IS_DELETED = #{isDeleted}")
  @Results({
      @Result(property = "repeatDays", column = "REPEAT_DAYS", typeHandler = ListStringTypeHandler.class)
  })
  PlanDTO detailPlanByPlanIdx(int planIdx, int isDeleted);
  // 활동 상세 보기
  @Select("SELECT * FROM PLAN_ACTIVITY WHERE PLAN_IDX = #{planIdx}")
  List<ActivityDTO> detailActivityByPlanIdx(int planIdx);
  // 기타 직업 조회
  JobEtcCateDTO getJobEtcInfoByPlanIdx(int planIdx);
  // 일반 직업 조회
  JobCateDTO getJOBCategoryByJobIdx(int jobIdx);
  // 관심사 조회
  TargetCateDTO getTargetCategoryByTargetIdx(int targetIdx);

  // 작성자 인덱스 조회
  @Select("SELECT USER_IDX FROM PLAN_TABLE WHERE PLAN_IDX = #{planIdx}")
  int getUserIdxByPlanIDx(int planIdx);
  // 루틴 횟수 조회
  @Select("SELECT COUNT(*) FROM PLAN_TABLE WHERE USER_IDX = #{userIdx} AND IS_COMPLETED = #{isCompleted} AND IS_DELETED = #{isDeleted}")
  int getCntOfPlanByUserIdxNIsCompleted(int userIdx, int isCompleted, int isDeleted);

  // 루틴 존재 여부 확인
  @Select("SELECT COUNT(*) FROM PLAN_TABLE WHERE PLAN_IDX = #{planIdx} AND IS_DELETED = #{isDeleted}")
  boolean checkPlanByPlanIdx(int planIdx, int isDeleted);
  // 활동 존재 여부 확인
  @Select("SELECT COUNT(*) FROM PLAN_ACTIVITY WHERE PLAN_IDX = #{planIdx} AND ACTIVITY_IDX = #{activityIdx}")
  boolean checkActByActivityIdx(int planIdx, int activityIdx);
  // 기타 직업 존재 여부 확인
  @Select("SELECT COUNT(*) FROM JOB_ETC_CATEGORY WHERE PLAN_IDX =#{planIdx}")
  boolean checkJobEtcByPlanIdx(int planIdx);
  // 추천 존재 여부 확인
  @Select("SELECT COUNT(*) FROM LIKE_TABLE WHERE PLAN_IDX = #{planIdx} AND USER_IDX = #{userIdx}")
  boolean checkLikeByPlanIdxNUserIdx(PlanRequestDTO planRequestDTO);

  // 루틴 수정하기
  void modifyPlan(PlanDTO planDTO);
  // 활동 수정하기
  void modifyActivity(ActivityDTO activityDTO);
  // 기타 직업 수정하기
  void modifyJobEtc(JobEtcCateDTO jobEtcCateDTO);
  // 추천 횟수 업데이트
  void modifyLikeCount(int planIdx);
  // 조회수 업데이트
  void increaseView(int planIdx);

  // 루틴 수정 시 활동 삭제
  @Delete("DELETE FROM PLAN_ACTIVITY WHERE ACTIVITY_IDX = #{activityIdx}")
  void deleteActByActivityIdx(int activityIdx);

  // 루틴 삭제 처리
  void deletePlan(@Param("planIdx") int planIdx,@Param("userIdx") int userIdx);

  // 루틴 활성화 / 비활성화 하기
  void goStopPlan(@Param("planIdx") int planIdx, @Param("userIdx") int userIdx, @Param("isActive") int isActive);

  // 루틴 추천
  void likePlan(PlanRequestDTO planRequestDTO);
  // 추천 취소
  @Delete("DELETE FROM LIKE_TABLE WHERE PLAN_IDX = #{planIdx} AND USER_IDX = #{userIdx}")
  void unLikePlan(PlanRequestDTO planRequestDTO);
}