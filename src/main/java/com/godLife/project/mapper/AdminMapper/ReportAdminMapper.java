package com.godLife.project.mapper.AdminMapper;

import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportAdminMapper {
  List<UserReportDTO> getAllReports(@Param("offset") int offset, @Param("limit") int limit);
  int countAllReports();
  List<UserReportDTO> selectReportsByStatus(@Param("status") int status, @Param("offset") int offset, @Param("limit") int limit);

  List<PlanReportDTO> selectPlanReports(@Param("offset") int offset, @Param("limit") int limit);
  List<PlanReportDTO> selectPlanReportsByStatus(@Param("status") int status, @Param("offset") int offset, @Param("size") int size);
  int countAllPlanReports();
}
