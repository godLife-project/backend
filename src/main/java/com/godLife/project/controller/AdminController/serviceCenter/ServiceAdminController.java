package com.godLife.project.controller.AdminController.serviceCenter;

import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.AdminInterface.serviceCenter.ServiceAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/service")
@Slf4j
@RequiredArgsConstructor
public class ServiceAdminController {

  private final GlobalExceptionHandler handler;
  private final ServiceAdminService serviceAdminService;

  @PatchMapping("/admin/switch/status")
  public ResponseEntity<Map<String ,Object>> switchStatus(@RequestHeader("Authorization") String authHeader) {
    int userIdx = handler.getUserIdxFromToken(authHeader);

    String result = serviceAdminService.switchAdminStatus(userIdx);

    return ResponseEntity.status(HttpStatus.OK).body(handler.createResponse(200, "상태 전환 완료 현재 상태 ::> " + result));
  }

//  @GetMapping("/admin/detail/{qnaIdx}")
//  public ResponseEntity<Map<String, Object>> getQnaDetails(@PathVariable int qnaIdx,
//                                                           @RequestHeader("Authorization") String authHeader) {
//    int userIdx = handler.getUserIdxFromToken(authHeader);
//
//
//
//  }

}
