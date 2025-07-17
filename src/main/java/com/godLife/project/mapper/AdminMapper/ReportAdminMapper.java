package com.godLife.project.mapper.AdminMapper;

import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportAdminMapper {
  List<UserReportDTO> getAllReports(@Param("offset") int offset, @Param("limit") int limit);
  List<UserReportDTO> selectReportsByStatus(@Param("status") int status, @Param("offset") int offset, @Param("limit") int limit);
  int countAllReports();
  int countReportsByStatus(@Param("status") int status);

  int countAllPlanReports();                    // 전체 신고 수
  int countPlanReportsByStatus(int status);     // 상태별 신고 수
  // 전체 신고 리스트 (루틴)
  List<PlanReportDTO> selectPlanReports(@Param("offset") int offset, @Param("limit") int limit);
  // 상태별 신고 리스트 (루틴)
  List<PlanReportDTO> selectPlanReportsByStatus(@Param("status") int status, @Param("offset") int offset, @Param("limit") int limit);

  // 신고 상태 처리
  void userReportStateUpdate(UserReportDTO userReportDTO);
  // 신고 인덱스로 피신고자(REPROTED_IDX) 찾기
  int findReportedIdxByReportIdx(@Param("userReportIdx") int userReportIdx);
  // 신고 누적 수 증가
  void incrementReportCount(@Param("userIdx") int userIdx);
  // 현재 신고 누적 수 조회
  Integer getReportCount(@Param("userIdx") int userIdx);
  // 유저 정지 처리
  void banUser(@Param("userIdx") int userIdx);


  // 신고 처리
  void planReportStateUpdate(PlanReportDTO planReportDTO);
  Integer getPlanIdxByReportIdx(int planReportIdx);   // 루틴 idx 조회
  void updatePlanVisible(PlanReportDTO planReportDTO);

}
