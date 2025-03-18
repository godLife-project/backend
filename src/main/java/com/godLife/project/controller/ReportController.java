package com.godLife.project.controller;


import com.godLife.project.dto.infos.PlanReportDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
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

  @PostMapping("/auth/plan/{planIdx}")
  ResponseEntity<Map<String, Object>> planReport(@RequestHeader("Authorization") String authHeader,
                                                 @PathVariable int planIdx,
                                                 @RequestBody PlanReportDTO planReportDTO) {
    int userIdx = handler.getUsernameFromToken(authHeader);
    planReportDTO.setReporterIdx(userIdx);
    planReportDTO.setPlanIdx(planIdx);

    
  }

}
