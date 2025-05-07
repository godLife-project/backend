package com.godLife.project.controller.AdminController;

import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;
import com.godLife.project.service.interfaces.AdminInterface.ReportAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/report")
public class ReportAdminController {
  private final ReportAdminService reportAdminService;

  public ReportAdminController(ReportAdminService reportAdminService) {
    this.reportAdminService = reportAdminService;
  }

  @GetMapping("/userReport")
  public Map<String, Object> getAllReports(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(required = false) Integer status) {

    // 전체 신고 수
    int total = reportAdminService.countAllReports();

    // 페이징된 신고 리스트 조회
    List<UserReportDTO> reports;
    if (status == null) {
      reports = reportAdminService.getAllReports(page, size);
    } else {
      reports = reportAdminService.getReportsByStatus(status, page, size);
    }

    // 응답용 Map 생성
    Map<String, Object> response = new HashMap<>();
    response.put("page", page);
    response.put("size", size);
    response.put("total", total);
    response.put("list", reports);

    return response;
  }

  @GetMapping("/planReport")
  public Map<String, Object> getAllPlanReports(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(required = false) Integer status) {

    // 전체 신고 수 조회
    int total = reportAdminService.countAllPlanReports();

    // 페이징된 신고 리스트 조회
    List<PlanReportDTO> reports;
    if (status == null) {
      reports = reportAdminService.getAllPlanReports(page, size);
    } else {
      reports = reportAdminService.getPlanReportsByStatus(status, page, size);
    }

    // 응답용 Map 생성
    Map<String, Object> response = new HashMap<>();
    response.put("page", page);
    response.put("size", size);
    response.put("total", total);
    response.put("list", reports);

    return response;
  }
}

