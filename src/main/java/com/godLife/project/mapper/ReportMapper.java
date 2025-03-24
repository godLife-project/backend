package com.godLife.project.mapper;

import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ReportMapper {
  // 루틴 신고하기
  @Insert("INSERT INTO PLAN_REPORT(REPORTER_IDX, PLAN_IDX, REPORT_REASON) VALUES (#{reporterIdx}, #{planIdx}, #{reportReason})")
  void planReport(PlanReportDTO planReportDTO);
  // 루틴 신고 취소하기
  @Update("UPDATE PLAN_REPORT SET STATUS = 3 WHERE PLAN_IDX = #{planIdx} AND REPORTER_IDX = #{reporterIdx} AND STATUS = 0")
  int planReportCancel(PlanReportDTO planReportDTO);
  // 루틴 상태 조회
  @Select("SELECT STATUS FROM PLAN_REPORT WHERE PLAN_IDX = #{planIdx} AND REPORTER_IDX = #{reporterIdx}")
  int getStatusAtPlanReport(PlanReportDTO planReportDTO);
  // 루틴 재신고
  @Update("UPDATE PLAN_REPORT SET REPORT_REASON = #{reportReason}, STATUS = 0 WHERE PLAN_IDX = #{planIdx} AND REPORTER_IDX = #{reporterIdx} AND STATUS = 3")
  void rePlanReport(PlanReportDTO planReportDTO);

  // 유저 신고하기
  @Insert("INSERT INTO USER_REPORT(REPORTER_IDX, REPORTED_IDX, REPORT_REASON) VALUES (#{reporterIdx}, #{reportedIdx}, #{reportReason})")
  void userReport(UserReportDTO userReportDTO);
  // 루틴 신고 취소하기
  @Update("UPDATE USER_REPORT SET STATUS = 3 WHERE REPORTER_IDX = #{reporterIdx} AND REPORTED_IDX = #{reportedIdx} AND STATUS = 0")
  int userReportCancel(UserReportDTO userReportDTO);
  // 유저 상태 조회
  @Select("SELECT STATUS FROM USER_REPORT WHERE REPORTER_IDX = #{reporterIdx} AND REPORTED_IDX = #{reportedIdx}")
  int getStatusAtUserReport(UserReportDTO userReportDTO);
  // 유저 재신고
  @Update("UPDATE USER_REPORT SET REPORT_REASON = #{reportReason}, STATUS = 0 WHERE REPORTER_IDX = #{reporterIdx} AND STATUS = 3")
  void reUserReport(UserReportDTO userReportDTO);
}
