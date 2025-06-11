package com.godLife.project.controller.websocket;

import com.godLife.project.dto.test.TestChatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminChatController {

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
