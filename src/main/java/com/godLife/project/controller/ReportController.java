package com.godLife.project.controller;


import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.jwtInterface.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

  @Autowired
  private GlobalExceptionHandler handler;

  private final ReportService reportService;

  // 루틴 신고하기
  @PostMapping("/auth/plan/{planIdx}")
  ResponseEntity<Map<String, Object>> planReport(@RequestHeader("Authorization") String authHeader,
                                                 @PathVariable int planIdx,
                                                 @RequestBody PlanReportDTO planReportDTO) {
    int userIdx = handler.getUsernameFromToken(authHeader);
    planReportDTO.setReporterIdx(userIdx);
    planReportDTO.setPlanIdx(planIdx);

    int result = reportService.planReport(planReportDTO);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "루틴 신고를 정상적으로 접수했습니다.";
      case 404 -> msg = "신고하신 루틴이 존재하지 않습니다.";
      case 409 -> msg = "이미 신고한 루틴입니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 루틴을 신고 하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result))
        .body(handler.createResponse(result, msg));
  }

  @PatchMapping("/auth/plan/cancel/{planIdx}")
  ResponseEntity<Map<String, Object>> planReportCancel(@RequestHeader("Authorization") String authHeader,
                                                       @PathVariable int planIdx) {
    int userIdx = handler.getUsernameFromToken(authHeader);

    PlanReportDTO planReportDTO = new PlanReportDTO();

    planReportDTO.setReporterIdx(userIdx);
    planReportDTO.setPlanIdx(planIdx);
    planReportDTO.setStatus(3);

    int result = reportService.planReportCancel(planReportDTO);

    // 응답 메세지 세팅
    String msg = "";
    switch (result) {
      case 200 -> msg = "루틴 신고가 정상적으로 취소됐습니다.";
      case 404 -> msg = "취소할 루틴이 존재하지 않거나, 이미 처리된 신고 또는 아직 신고하지 않은 루틴입니다.";
      case 500 -> msg = "서버 내부적으로 오류가 발생하여 루틴을 신고 하지 못했습니다.";
      default -> msg = "알 수 없는 오류가 발생했습니다.";
    }

    // 응답 메시지 설정
    return ResponseEntity.status(handler.getHttpStatus(result))
        .body(handler.createResponse(result, msg));
  }

}
