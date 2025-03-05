package com.godLife.project.controller;


import com.godLife.project.dto.test.GetPlanIdxDTO;
import com.godLife.project.dto.test.GetUserListDTO;
import com.godLife.project.service.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

  private final TestService testService;

  public TestController(TestService testService) {
    this.testService = testService;
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
}
