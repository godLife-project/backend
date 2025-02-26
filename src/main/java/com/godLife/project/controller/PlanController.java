package com.godLife.project.controller;


import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/plan")
public class PlanController {

  private final PlanService planService;

  public PlanController(PlanService planService) {
    this.planService = planService;
  }

  @PostMapping("/write")
  public ResponseEntity<Map<String, Object>> write(@RequestBody PlanDTO planDTO) {

    boolean result = planService.insertPlanWithAct(planDTO);

    Map<String, Object> message = new HashMap<>();
    if (result) {
      message.put("status", "success");
      message.put("message", "루틴 저장 완료");
      message.put("code", 200);
      return ResponseEntity.ok(message);
    }
    else {
      message.put("status", "error");
      message.put("message", "서버 내부적으로 오류가 발생하여 루틴을 저장하지 못했습니다");
      message.put("code", 500);
      return ResponseEntity.internalServerError().body(message);
    }

  }
}
