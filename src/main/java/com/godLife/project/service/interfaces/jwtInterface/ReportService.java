package com.godLife.project.service.interfaces.jwtInterface;

import com.godLife.project.dto.infos.PlanReportDTO;

public interface ReportService {
  // 루틴 신고하기
  int planReport(PlanReportDTO planReportDTO);

  int planReportCancel(PlanReportDTO planReportDTO);
}
