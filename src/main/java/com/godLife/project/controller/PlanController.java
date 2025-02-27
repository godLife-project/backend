package com.godLife.project.controller;


import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/plan")
public class PlanController {

  private final PlanService planService;

  public PlanController(PlanService planService) {
    this.planService = planService;
  }

  // 루틴 작성 API
  @PostMapping("/write")
  public ResponseEntity<Map<String, Object>> write(@Valid @RequestBody PlanDTO writePlanDTO, BindingResult result) {

    if (result.hasErrors()) {
      Map<String, Object> errors = new HashMap<>();
      result.getFieldErrors().forEach(fieldError -> {
        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
      });

      return ResponseEntity.badRequest().body(errors);
    }
    else {
      boolean insertResult = planService.insertPlanWithAct(writePlanDTO);

      Map<String, Object> message = new HashMap<>();
      if (insertResult) {
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

  @GetMapping("/detail/{planIdx}")
  public ResponseEntity<Map<String, Object>> detail(@PathVariable int planIdx) {
    Map<String, Object> message = new HashMap<>();
    try {
      // 해당 인덱스의 루틴 조회
      PlanDTO planDTO = planService.detailRoutine(planIdx);

      message.put("status", "success");
      message.put("message", "루틴 조회 완료");
      message.put("routineDetails", planDTO);
      message.put("code", 200);

      return ResponseEntity.ok(message);
    } catch (Exception e) {

      message.put("status", "error");
      message.put("message", "루틴 조회 실패,, 조회하려는 루틴이 존재하지 않습니다.");
      message.put("code", 400);

      return ResponseEntity.badRequest().body(message);
    }
  }

  // JSON 파싱 오류 처리 메소드
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleJsonParseException(HttpMessageNotReadableException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", "error");
    errorResponse.put("message", "잘못된 JSON 형식입니다. 필드값이 누락되었거나, 필드명의 오타가 있을 수 있습니다.");
    errorResponse.put("code", 400);

    return ResponseEntity.badRequest().body(errorResponse);
  }

}
