package com.godLife.project.controller.AdminController.serviceCenter;

import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminInfos;
import com.godLife.project.dto.serviceAdmin.ServiceCenterAdminList;
import com.godLife.project.enums.WSDestination;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.listener.QnaQueueListener;
import com.godLife.project.service.impl.websocketImpl.WebSocketMessageService;
import com.godLife.project.service.interfaces.AdminInterface.serviceCenter.ServiceAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/service/admin")
@Slf4j
@RequiredArgsConstructor
public class ServiceAdminController {

  private final GlobalExceptionHandler handler;
  private final ServiceAdminService serviceAdminService;

  private final QnaQueueListener qnaQueueListener;
  private final WebSocketMessageService messageService;

  // 관리자 상태 조회
  @GetMapping("/get/status")
  public ResponseEntity<Map<String ,Object>> getStatus(@RequestHeader("Authorization") String authHeader) {
    int userIdx = handler.getUserIdxFromToken(authHeader);

    String result = serviceAdminService.getAdminStatus(userIdx);

    return ResponseEntity.status(HttpStatus.OK).body(handler.createResponse(200, result));
  }

  // 관리자 상태 전환
  @PatchMapping("/switch/status")
  public ResponseEntity<Map<String ,Object>> switchStatus(@RequestHeader("Authorization") String authHeader) {
    int userIdx = handler.getUserIdxFromToken(authHeader);
    String username = handler.getUserNameFromToken(authHeader);

    String result = serviceAdminService.switchAdminStatus(userIdx);

    if ("활성화".equals(result)) {
      qnaQueueListener.wakeUp(userIdx, username);
    }

    // 클라이언트에게 관리자 목록 전송
    List<ServiceCenterAdminInfos> accessAdminInfos = serviceAdminService.getAllAccessServiceAdminList();

    List<ServiceCenterAdminList> accessAdminList = serviceAdminService.getAccessAdminListForMessage(accessAdminInfos);
    messageService.sendToAll(WSDestination.ALL_ACCESS_ADMIN_LIST.getDestination(), accessAdminList);

    return ResponseEntity.status(HttpStatus.OK).body(handler.createResponse(200, result));
  }

  // 오토 매칭 쓰레드 강제 활성화
  @PostMapping("/autoMatch/wakeUp")
  public ResponseEntity<Map<String, Object>> wakeUpAutoMatch(@RequestHeader("Authorization") String authHeader) {
    String username = handler.getUserNameFromToken(authHeader);
    int userIdx = handler.getUserIdxFromToken(authHeader);

    qnaQueueListener.wakeUp(userIdx, username);

    return ResponseEntity.status(HttpStatus.OK).body(handler.createResponse(200, "QnA 자동 매칭을 깨웠습니다."));
  }

}
