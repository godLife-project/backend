package com.godLife.project.mapper;

import com.godLife.project.dto.infos.PlanReportDTO;
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
}
