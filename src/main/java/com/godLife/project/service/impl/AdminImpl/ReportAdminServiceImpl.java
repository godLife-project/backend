package com.godLife.project.service.impl.AdminImpl;

import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;
import com.godLife.project.mapper.AdminMapper.ReportAdminMapper;
import com.godLife.project.service.interfaces.AdminInterface.ReportAdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportAdminServiceImpl implements ReportAdminService {

  private ReportAdminMapper reportAdminMapper;

  public ReportAdminServiceImpl(ReportAdminMapper reportAdminMapper) {
    this.reportAdminMapper = reportAdminMapper;
  }

  public List<UserReportDTO> getAllReports(int page, int size) {
    int offset = (page - 1) * size;
    return reportAdminMapper.getAllReports(offset, size);
  }

  public int countAllReports() {
    return reportAdminMapper.countAllReports();
  }

  public List<UserReportDTO> getReportsByStatus(int status, int page, int size) {
    int offset = (page - 1) * size;
    return reportAdminMapper.selectReportsByStatus(status, offset, size);
  }



  public List<PlanReportDTO> getAllPlanReports(int page, int size) {
    int offset = (page - 1) * size;
    return reportAdminMapper.selectPlanReports(offset, size);
  }

  public List<PlanReportDTO> getPlanReportsByStatus(int status, int page, int size) {
    int offset = (page - 1) * size;
    return reportAdminMapper.selectPlanReportsByStatus(status, offset, size);
  }

  public int countAllPlanReports() {
    return reportAdminMapper.countAllPlanReports();
  }

}

