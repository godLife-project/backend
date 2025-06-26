package com.godLife.project.service.interfaces.AdminInterface;

import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportAdminService {
  List<UserReportDTO> getAllReports(int page, int size);
  List<UserReportDTO> getReportsByStatus(int status, int page, int size);
  int countAllReports();
  int countReportsByStatus(int status);

  // 전체 루틴 신고 수
  int countAllPlanReports();
  // 상태별 루틴 신고 수
  int countPlanReportsByStatus(int status);
  // 전체 루틴 신고 리스트
  List<PlanReportDTO> getAllPlanReports(int offset, int limit);
  // 상태별 루틴 신고 리스트
  List<PlanReportDTO> getPlanReportsByStatus(int status, int offset, int limit);

void userReportStateUpdate(UserReportDTO userReportDTO);
  int planReportStateUpdate (PlanReportDTO planReportDTO, int userIdx);

}

