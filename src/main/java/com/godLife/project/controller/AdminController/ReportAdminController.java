package com.godLife.project.controller.AdminController;

import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.dto.infos.UserReportDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.AdminInterface.ReportAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

  // 유저 신고 조회 (전체 or 상태별)
  @GetMapping("/userReport")
  public Map<String, Object> getAllReports(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(required = false) Integer status) {

    // 전체 신고 수 조회 (전체 또는 상태별)
    int total = (status == null)
            ? reportAdminService.countAllReports()
            : reportAdminService.countReportsByStatus(status);

    // 페이징된 신고 리스트 조회 (상태별 또는 전체)
    List<UserReportDTO> reports = reportAdminService.getAllReports(status, page, size);

    Map<String, Object> response = new HashMap<>();
    response.put("total", total);
    response.put("reports", reports);
    response.put("page", page);
    response.put("size", size);

    return response;
  }

  @PostMapping("/userReportState")
  public ResponseEntity<Map<String, Object>> userReportStateUpdate(
          @RequestParam("userReportIdx") int userReportIdx,
          @RequestParam("isApproved") int isApproved) {
    try {
      UserReportDTO dto = new UserReportDTO();
      dto.setUserReportIdx(userReportIdx);
      dto.setIsApproved(isApproved);

      reportAdminService.userReportStateUpdate(dto); // 내부에서 status=1로 처리

      String message = (isApproved == 1) ? "신고가 승인되어 처리 완료되었습니다." : "신고가 거절되어 처리 완료되었습니다.";
      return ResponseEntity.ok(handler.createResponse(200, message));

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
  public Map<String, Object> getPlanReports(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(required = false) Integer status) {

    int offset = (page - 1) * size;

    //  전체 또는 상태별 루틴 신고 수 조회
    int total = (status == null)
            ? reportAdminService.countAllPlanReports()
            : reportAdminService.countPlanReportsByStatus(status);

    //  전체 또는 상태별 루틴 신고 리스트 조회
    List<PlanReportDTO> reports = (status == null)
            ? reportAdminService.getAllPlanReports(offset, size)
            : reportAdminService.getPlanReportsByStatus(status, offset, size);

    // 응답 구성
    Map<String, Object> response = new HashMap<>();
    response.put("page", page);
    response.put("size", size);
    response.put("total", total);
    response.put("list", reports);

    return response;
  }

  // 루틴 신고 처리
  @PatchMapping("/plans/{planIdx}/reports/{planReportIdx}/status")
  public ResponseEntity<?> updateReportStatus(
          @RequestHeader("Authorization") String authHeader,
          @PathVariable int planIdx,
          @PathVariable int planReportIdx,
          @RequestBody PlanReportDTO planReportDTO) {

    int userIdx = handler.getUserIdxFromToken(authHeader);
    planReportDTO.setPlanReportIdx(planReportIdx);
    planReportDTO.setPlanIdx(planIdx);

    int result = reportAdminService.planReportStateUpdate(planReportDTO, userIdx);

    return switch (result) {
      case 200 -> ResponseEntity.ok("신고가 처리되었고 루틴은 비공개로 전환되었습니다.");
      case 403 -> ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
      default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
    };
}
}

