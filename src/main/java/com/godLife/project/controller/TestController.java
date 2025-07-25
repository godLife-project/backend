package com.godLife.project.controller;


import com.godLife.project.dto.test.GetPlanIdxDTO;
import com.godLife.project.dto.test.GetUserListDTO;
import com.godLife.project.service.impl.scheduleImpl.RoutineScheduleServiceImpl;
import com.godLife.project.service.interfaces.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestController {

  private final TestService testService;
  private final RoutineScheduleServiceImpl routineScheduleService;

  public TestController(TestService testService, RoutineScheduleServiceImpl routineScheduleService) {
    this.testService = testService;
    this.routineScheduleService = routineScheduleService;
  }

  @GetMapping("/test1")
  public ResponseEntity<Map<String, String>> Test() {
    Map<String, String> response = new HashMap<>();
    response.put("message", "hello!!!!!");

    return ResponseEntity.ok(response);
  }

  @GetMapping("/auth/test2")
  public ResponseEntity<Map<String, String>> TestP() {
    Map<String, String> response = new HashMap<>();
    response.put("message", "You have JWT access Token");

    return ResponseEntity.ok(response);
  }

  @GetMapping("/get/planIdx")
  public ResponseEntity<List<GetPlanIdxDTO>> getAllPlanIdx() {
    try {
      List<GetPlanIdxDTO> planIdxList = testService.findPlanIdx();

      if (planIdxList.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(planIdxList);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/get/userList")
  public ResponseEntity<List<GetUserListDTO>> getAllUserList() {
    try {
      List<GetUserListDTO> userListDTOS = testService.getUserList();

      if (userListDTOS.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(userListDTOS);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PatchMapping("/delete/review/{planIdx}")
  public ResponseEntity<String> deleteReview(@PathVariable int planIdx) {
    try {
      testService.deleteReview(planIdx);
      return ResponseEntity.ok().build();

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PatchMapping("/set/plan/status/{planIdx}")
  public ResponseEntity<String> changePlanStatus(@PathVariable int planIdx,
                                                 @RequestParam int isActive,
                                                 @RequestParam int isCompleted,
                                                 @RequestParam int isDeleted,
                                                 @RequestParam int isShared) {
    try {
      testService.changePlanStatus(isActive, isCompleted, isDeleted, isShared, planIdx);
      return ResponseEntity.ok().build();

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }



  @GetMapping("/apiCheck")
  public ResponseEntity<String> apicheck() {
    try {
      routineScheduleService.clearComboWhenAllFireIsActivatedOfPlan();
      return ResponseEntity.ok().build();

    } catch (Exception e) {
      log.error("e: ", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
  }
}
