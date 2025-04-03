package com.godLife.project.controller;

import com.godLife.project.dto.list.MyPlanDTO;
import com.godLife.project.dto.list.PlanListDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.ListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/list")
public class ListController {

  @Autowired
  private final GlobalExceptionHandler handler;

  private final ListService listService;

  @GetMapping("/auth/myPlans")
  public ResponseEntity<Map<String, Object>> listMyPlans(@RequestHeader("Authorization") String authHeader) {
    try {
      // userIdx 조회
      int userIdx = handler.getUserIdxFromToken(authHeader);
      // 루틴 리스트 조회
      List<MyPlanDTO> myPlanList = listService.getMyPlansList(userIdx);

      if (myPlanList == null) {
        System.out.println("진행/대기중 루틴의 리스트 조회 중...서버 오류 발생!!!!!");
        throw new Exception("서버 내부 오류로 인해 루틴 리스트 조회에 실패했습니다.");
      }
      if (myPlanList.isEmpty()) {
        throw new NoSuchElementException("진행/대기중인 루틴 없음.");
      }
      // 응답 메시지 설정
      return ResponseEntity.ok().body(handler.createResponse(200, myPlanList));

    } catch (NoSuchElementException e) {
      return ResponseEntity.status(handler.getHttpStatus(204)).build();

    } catch (Exception e) {
      String msg = "서버 내부 오류로 인해 루틴 리스트 조회에 실패했습니다.";
      log.error("e: ", e);
      return ResponseEntity.status(handler.getHttpStatus(500)).body(handler.createResponse(500, msg));
    }
  }

  @GetMapping("/plan/{mode}")
  public ResponseEntity<Map<String, Object>> listAllPlans(@PathVariable String mode,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "0") int status,
                                                          @RequestParam(required = false) List<Integer> target,
                                                          @RequestParam(required = false) List<Integer> job,
                                                          @RequestParam(defaultValue = "latest") String sort,
                                                          @RequestParam(defaultValue = "desc") String order,
                                                          @RequestParam(required = false) String search) {

    //System.out.println("--컨트롤러--");
    //System.out.println(page + " " +  size + " " + status + " " + target + " " + job + " " + sort + " " + order);

    Map<String, Object> response = listService.getAllPlansList(mode, page - 1, size, status, target, job, sort, order, search, 0);

    Object plans = response.get("plans");

    if (plans instanceof List<?>) {
      List<PlanListDTO> tempList = ((List<?>) plans).stream()
          .filter(PlanListDTO.class::isInstance)  // PlanListDTO 타입만 필터링
          .map(PlanListDTO.class::cast)
          .toList();
      if (tempList.isEmpty()) {
        return ResponseEntity.status(handler.getHttpStatus(204)).build();
      }
    }

    return ResponseEntity.ok(response);
  }

  /* -----------------------------------------// 함수 구현 //------------------------------------------------------- */

  /* --------------------------------------------------------------------------------------------------------------- */
}
