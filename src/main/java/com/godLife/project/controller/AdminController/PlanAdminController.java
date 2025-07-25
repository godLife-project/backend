package com.godLife.project.controller.AdminController;

import com.godLife.project.dto.categories.JobEtcCateDTO;
import com.godLife.project.dto.datas.ActivityDTO;
import com.godLife.project.dto.datas.PlanDTO;
import com.godLife.project.dto.list.customDTOs.CustomAdminPlanListDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.mapper.AdminMapper.PlanAdminMapper;
import com.godLife.project.service.interfaces.AdminInterface.PlanAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/plans")
public class PlanAdminController {
  private final PlanAdminService planAdminService;

  private final GlobalExceptionHandler handler;



  // ----------- 관리자가 작성한 루틴 리스트 조회 ---------------
  @GetMapping
  public ResponseEntity<Map<String, Object>> selectAdminPlanList(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size) {

    List<CustomAdminPlanListDTO> plans = planAdminService.selectAdminPlanList(page, size);
    int totalPlans = planAdminService.getTotalAdminPlanCount();
    int totalPages = (int) Math.ceil((double) totalPlans / size);

    if (plans.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
              .body(createResponse(204, "관리자 루틴이 없습니다."));
    }

    Map<String, Object> response = createResponse(200, "관리자 루틴 조회 성공");
    response.put("plans", plans);
    response.put("totalPages", totalPages);
    response.put("currentPage", page);
    response.put("pageSize", size);

    return ResponseEntity.ok(response);
  }

  private Map<String, Object> createResponse(int statusCode, String message) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", statusCode);
    response.put("message", message);
    return response;
  }

  // ----------- 관리자가 작성한 루틴 카테고리 리스트 조회 ---------------
  @GetMapping("/{targetIdx}")
  public ResponseEntity<Map<String, Object>> getAllAdminPlans(
          @PathVariable Integer targetIdx,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size) {

    List<CustomAdminPlanListDTO> plans = planAdminService.selectAdminPlanListByTargetIdx(targetIdx, page, size);
    int totalPlans = planAdminService.getTotalAdminPlanCount();
    int totalPages = (int) Math.ceil((double) totalPlans / size);

    if (plans.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
              .body(createResponse(204, "카테고리별 관리자 루틴이 없습니다."));
    }

    Map<String, Object> response = createResponse(200, "카테고리별 관리자 루틴 조회 성공");
    response.put("plans", plans);
    response.put("totalPages", totalPages);
    response.put("currentPage", page);
    response.put("pageSize", size);

    return ResponseEntity.ok(response);
  }
  //    ----------- 상세보기는 해당 url 사용 : api/plan/detail/{planIdx}

  // 전체 조회
  @GetMapping("/all")
  public ResponseEntity<Map<String, Object>> selectPlanList(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size) {

    List<CustomAdminPlanListDTO> plans = planAdminService.selectPlanList(page, size);
    int totalPlans = planAdminService.getTotalAdminPlanCount();
    int totalPages = (int) Math.ceil((double) totalPlans / size);

    if (plans.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
              .body(createResponse(204, "루틴이 없습니다."));
    }

    Map<String, Object> response = createResponse(200, "루틴 조회 성공");
    response.put("plans", plans);
    response.put("totalPages", totalPages);
    response.put("currentPage", page);
    response.put("pageSize", size);

    return ResponseEntity.ok(response);
  }

}

