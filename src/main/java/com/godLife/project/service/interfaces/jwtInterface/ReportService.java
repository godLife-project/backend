package com.godLife.project.service.interfaces.jwtInterface;

import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;

public interface ReportService {
  // 루틴 신고하기
  int planReport(PlanReportDTO planReportDTO);
  // 루틴 신고 취소
  int planReportCancel(PlanReportDTO planReportDTO);

  // 유저 신고하기
  int userReport(UserReportDTO userReportDTO);
  // 유저 신고 취소
  int userReportCancel(UserReportDTO userReportDTO);

}
