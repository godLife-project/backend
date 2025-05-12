package com.godLife.project.controller.AdminController;

import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.AdminInterface.ReportAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/report")
public class ReportAdminController {
  @Autowired
  private GlobalExceptionHandler handler;

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
    int total = (status == null)
            ? reportAdminService.countAllReports()
            : reportAdminService.countReportsByStatus(status);

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

  // 유저 신고 처리
  @PostMapping("/userReportState")
  public ResponseEntity<Map<String, Object>> userReportStateUpdate(
          @RequestParam("userReportIdx") int userReportIdx,
          @RequestParam("status") int status) {
    try {
      UserReportDTO dto = new UserReportDTO();
      dto.setUserReportIdx(userReportIdx);
      dto.setStatus(status); // 클라이언트가 넘긴 값으로 처리

      reportAdminService.userReportStateUpdate(dto);

      String message = (status == 1) ? "신고가 처리 완료되었습니다." : "신고 상태가 미처리로 변경되었습니다.";
      return ResponseEntity.ok(
              handler.createResponse(200, message)
      );

    } catch (IllegalArgumentException e) {
      log.error("잘못된 요청 값으로 인한 처리 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(400))
              .body(handler.createResponse(400, "요청 오류: " + e.getMessage()));
    } catch (Exception e) {
      log.error("신고 상태 업데이트 중 서버 오류 발생: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류로 인해 신고 상태를 변경할 수 없습니다."));
    }
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

  // 루틴 신고 처리
  @PostMapping("/planReportState")
  public ResponseEntity<Map<String, Object>> planReportStateUpdate(
          @RequestParam("planReportIdx") int planReportIdx,
          @RequestParam("status") int status) {
    try {
      PlanReportDTO dto = new PlanReportDTO();
      dto.setPlanReportIdx(planReportIdx);
      dto.setStatus(status); // 클라이언트가 넘긴 값으로 처리

      reportAdminService.planReportStateUpdate(dto);

      String message = (status == 1) ? "신고가 처리 완료되었습니다." : "신고 상태가 미처리로 변경되었습니다.";
      return ResponseEntity.ok(
              handler.createResponse(200, message)
      );

    } catch (IllegalArgumentException e) {
      log.error("잘못된 요청 값으로 인한 처리 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(400))
              .body(handler.createResponse(400, "요청 오류: " + e.getMessage()));
    } catch (Exception e) {
      log.error("신고 상태 업데이트 중 서버 오류 발생: {}", e.getMessage(), e);
      return ResponseEntity.status(handler.getHttpStatus(500))
              .body(handler.createResponse(500, "서버 오류로 인해 신고 상태를 변경할 수 없습니다."));
    }
  }
}

