package com.godLife.project.service.interfaces.AdminInterface;

import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportAdminService {
  List<UserReportDTO> getAllReports(int page, int size);
  List<UserReportDTO> getReportsByStatus(int status, int page, int size);
  int countAllReports();

List<PlanReportDTO> getAllPlanReports(int page, int size);
List<PlanReportDTO> getPlanReportsByStatus(int status, int page, int size);
int countAllPlanReports();
}

