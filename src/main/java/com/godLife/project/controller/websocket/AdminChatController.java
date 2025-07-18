package com.godLife.project.controller.websocket;

import com.godLife.project.dto.request.adminChat.ChatCreateDTO;
import com.godLife.project.dto.test.TestChatDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.AdminInterface.AdminChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminChatController {

  private final GlobalExceptionHandler handler;
  private final AdminChatService adminChatService;


  // 채팅방 개설
  @PostMapping("/api/admin/chat")
  public ResponseEntity<Map<String, Object>> createChatRoom(@RequestHeader("Authorization") String authHeader,
                                                            @RequestBody ChatCreateDTO chatCreateDTO) {

    // 작성자 인덱스 번호 추출
    int adminIdx = handler.getUserIdxFromToken(authHeader);
    chatCreateDTO.setCreatedBy(adminIdx);

    // 채팅방 개설 로직 실행
    int roomIdx = adminChatService.createAdminChatRoom(chatCreateDTO);
    Map<String, Object> msg = new HashMap<>();
    msg.put("roomIdx", roomIdx);
    msg.put("notice", "채팅방이 생성되었습니다.");
    return ResponseEntity.ok().body(handler.createResponse(200, msg));
  }

  // 채팅 기능
  @MessageMapping("/roomChat/{roomNo}")
  @SendTo("/sub/roomChat/{roomNo}")
  public TestChatDTO broadcasting(final TestChatDTO request,
                                  @DestinationVariable(value = "roomNo") final String chatRoomNo) {
    request.setRoomNo(chatRoomNo);
    log.info("{roomNo : {}, request : {}}", chatRoomNo, request);

    return request;
  }
}
